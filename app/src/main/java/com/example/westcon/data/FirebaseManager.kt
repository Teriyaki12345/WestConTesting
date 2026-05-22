package com.example.westcon.data

import com.example.westcon.data.FirestoreCollections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val usersCollection by lazy { db.collection(FirestoreCollections.USERS) }
    private val skillsCollection by lazy { db.collection(FirestoreCollections.SKILLS) }
    private val freedomWallCollection by lazy { db.collection(FirestoreCollections.FREEDOM_WALL) }
    private val messagesCollection by lazy { db.collection(FirestoreCollections.MESSAGES) }
    private val chatSummariesCollection by lazy { db.collection(FirestoreCollections.CHAT_SUMMARIES) }
    private val notificationsCollection by lazy { db.collection(FirestoreCollections.NOTIFICATIONS) }

    // --- Authentication ---
    fun getCurrentUser() = auth.currentUser
    
    fun isUserLoggedIn() = auth.currentUser != null

    suspend fun signUp(email: String, pass: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, pass: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    // --- User Profile ---
    suspend fun checkUsernameExists(username: String): Boolean {
        return try {
            val query = usersCollection.whereEqualTo("name", username).get().await()
            !query.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            usersCollection.document(profile.uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            android.util.Log.d("FirebaseManager", "Fetching profile for uid: $uid")
            val snapshot = usersCollection.document(uid).get().await()
            val profile = snapshot.toObject(UserProfile::class.java)
            if (profile != null) {
                android.util.Log.d("FirebaseManager", "Profile fetched successfully for $uid: name=${profile.name}")
            } else {
                android.util.Log.w("FirebaseManager", "Profile is null for uid: $uid, snapshot exists: ${snapshot.exists()}")
            }
            profile
        } catch (e: Exception) {
            android.util.Log.e("FirebaseManager", "Error fetching profile for uid $uid: ${e.message}", e)
            null
        }
    }

    // --- Skill Marketplace ---
    suspend fun postSkill(post: SkillPost): Result<Unit> {
        return try {
            val ref = skillsCollection.document()
            ref.set(post.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSkillPost(postId: String): Result<Unit> {
        return try {
            skillsCollection.document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSkillPosts(): Flow<List<SkillPost>> = callbackFlow {
        val subscription = skillsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(SkillPost::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    // --- Freedom Wall ---
    suspend fun postToFreedomWall(post: FreedomPost): Result<Unit> {
        return try {
            val ref = freedomWallCollection.document()
            val authorUid = auth.currentUser?.uid ?: ""
            val profile = getUserProfile(authorUid)
            
            ref.set(post.copy(
                id = ref.id, 
                authorUid = authorUid,
                authorName = profile?.name ?: "User",
                authorIconName = profile?.profileIconName ?: "Person"
            )).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFreedomPost(postId: String): Result<Unit> {
        return try {
            freedomWallCollection.document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFreedomPosts(): Flow<List<FreedomPost>> = callbackFlow {
        val subscription = freedomWallCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(FreedomPost::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun toggleLikeFreedomPost(postId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val docRef = freedomWallCollection.document(postId)
            val snapshot = docRef.get().await()
            val post = snapshot.toObject(FreedomPost::class.java) ?: return Result.failure(Exception("Post not found"))
            
            val likedBy = post.likedBy.toMutableList()
            var likes = post.likes
            
            if (likedBy.contains(uid)) {
                likedBy.remove(uid)
                likes--
            } else {
                likedBy.add(uid)
                likes++
            }
            
            docRef.update("likedBy", likedBy, "likes", likes).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Messaging ---
    suspend fun sendMessage(msg: Message, chatId: String): Result<Unit> {
        return try {
            val ref = messagesCollection.document(chatId).collection("history").document()
            ref.set(msg.copy(id = ref.id)).await()
            
            // Also update chat summaries for both users
            try {
                startChat(msg.senderUid, msg.receiverUid, msg.text)
            } catch (e: Exception) {
                // Log error but don't fail the message sending
                android.util.Log.e("FirebaseManager", "Error updating chat summaries: ${e.message}", e)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseManager", "Error sending message: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun startChat(uid: String, otherUid: String, firstMsg: String) {
        try {
            updateChatSummary(uid, otherUid, firstMsg)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseManager", "Error updating chat summary for $uid: ${e.message}", e)
        }
        try {
            updateChatSummary(otherUid, uid, firstMsg)
        } catch (e: Exception) {
            android.util.Log.e("FirebaseManager", "Error updating chat summary for $otherUid: ${e.message}", e)
        }
    }

    private suspend fun updateChatSummary(uid: String, otherUid: String, lastMsg: String) {
        try {
            android.util.Log.d("FirebaseManager", "=== updateChatSummary START ===")
            android.util.Log.d("FirebaseManager", "Creating chat summary for user: $uid, with other user: $otherUid")
            
            // Try to fetch profile, but don't fail the chat summary update if it fails
            val profile = try {
                android.util.Log.d("FirebaseManager", "Attempting to fetch profile for: $otherUid")
                getUserProfile(otherUid)
            } catch (e: Exception) {
                android.util.Log.w("FirebaseManager", "Could not fetch profile for $otherUid: ${e.message}")
                null
            }
            
            val summary = ChatSummary(
                otherUserUid = otherUid,
                otherUserName = profile?.name ?: "User",
                otherUserIconName = profile?.profileIconName ?: "Person",
                otherUserDept = profile?.department ?: "WVSU",
                lastMessage = lastMsg,
                timestamp = com.google.firebase.Timestamp.now()
            )
            
            val path = "chat_summaries/$uid/chats/$otherUid"
            android.util.Log.d("FirebaseManager", "Writing chat summary to: $path")
            android.util.Log.d("FirebaseManager", "Summary data: otherUserName=${summary.otherUserName}, lastMessage=${summary.lastMessage}")
            
            chatSummariesCollection.document(uid).collection("chats").document(otherUid).set(summary).await()
            
            android.util.Log.d("FirebaseManager", "Chat summary created successfully for $uid with $otherUid")
            android.util.Log.d("FirebaseManager", "=== updateChatSummary END (SUCCESS) ===")
        } catch (e: Exception) {
            android.util.Log.e("FirebaseManager", "=== updateChatSummary END (FAILED) ===")
            android.util.Log.e("FirebaseManager", "Failed to create chat summary for $uid: ${e.message}", e)
        }
    }

    // --- Notifications ---
    suspend fun sendNotification(notification: Notification): Result<Unit> {
        return try {
            val ref = notificationsCollection.document()
            ref.set(notification.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNotification(id: String): Result<Unit> {
        return try {
            notificationsCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markNotificationAsRead(id: String): Result<Unit> {
        return try {
            notificationsCollection.document(id).update("isRead", true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAllNotificationsAsRead(): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val snapshot = notificationsCollection
                .whereEqualTo("receiverUid", uid)
                .whereEqualTo("isRead", false)
                .get()
                .await()

            if (snapshot.isEmpty) return Result.success(Unit)

            val batch = db.batch()
            for (doc in snapshot.documents) {
                batch.update(doc.reference, "isRead", true)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rateUserSkill(targetUid: String, skillName: String, rating: Double): Result<Unit> {
        return try {
            val profile = getUserProfile(targetUid) ?: return Result.failure(Exception("User not found"))
            val skills = profile.skillsToTeach.toMutableList()
            val skillIndex = skills.indexOfFirst { it.skillName.equals(skillName, ignoreCase = true) }
            
            if (skillIndex != -1) {
                val skill = skills[skillIndex]
                val newTotal = skill.totalRatings + 1
                val newAvg = ((skill.averageRating * skill.totalRatings) + rating) / newTotal
                
                // Automatic Level Progression (Simple Logic: every 3 ratings = +1 level)
                val newLevel = (newTotal / 3 + 1).coerceAtMost(5)
                
                skills[skillIndex] = skill.copy(
                    averageRating = newAvg,
                    totalRatings = newTotal,
                    level = newLevel
                )
                
                // Update overall user rating as well
                val overallAvg = skills.map { it.averageRating }.average()
                saveUserProfile(profile.copy(skillsToTeach = skills, rating = overallAvg))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val subscription = messagesCollection
            .document(chatId)
            .collection("history")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Message::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getChatSummaries(): Flow<List<ChatSummary>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val subscription = chatSummariesCollection
            .document(uid)
            .collection("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(ChatSummary::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    // --- Notifications ---
    fun getNotifications(): Flow<List<Notification>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        // Primary listener: ordered by timestamp (server-side). If Firestore requires
        // a composite index and fails, fallback to an unordered listener and sort
        // client-side to preserve UX without needing immediate index creation.
        var subscription: com.google.firebase.firestore.ListenerRegistration? = null

        subscription = notificationsCollection
            .whereEqualTo("receiverUid", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.w("FirebaseManager", "Notifications query failed: ${error.message}")
                    // Detect index-required failure and attach unordered fallback
                    if (error.message?.contains("requires an index") == true || error.message?.contains("index") == true) {
                        try {
                            // remove the failing registration and attach fallback
                            subscription?.remove()
                        } catch (t: Throwable) { /* ignore */ }

                        subscription = notificationsCollection
                            .whereEqualTo("receiverUid", uid)
                            .addSnapshotListener { snap2, err2 ->
                                if (err2 != null) {
                                    android.util.Log.e("FirebaseManager", "Fallback notifications listener failed: ${err2.message}", err2)
                                    return@addSnapshotListener
                                }
                                if (snap2 != null) {
                                    val list = snap2.toObjects(Notification::class.java).sortedByDescending { it.timestamp }
                                    trySend(list)
                                }
                            }
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    trySend(snapshot.toObjects(Notification::class.java))
                }
            }

        awaitClose { try { subscription?.remove() } catch (t: Throwable) { } }
    }
}
