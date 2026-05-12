package com.example.westcon.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            db.collection("users").document(profile.uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            db.collection("users").document(uid).get().await().toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // --- Skill Marketplace ---
    suspend fun postSkill(post: SkillPost): Result<Unit> {
        return try {
            val ref = db.collection("skills").document()
            ref.set(post.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSkillPosts(): Flow<List<SkillPost>> = callbackFlow {
        val subscription = db.collection("skills")
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
            val ref = db.collection("freedom_wall").document()
            ref.set(post.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFreedomPosts(): Flow<List<FreedomPost>> = callbackFlow {
        val subscription = db.collection("freedom_wall")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    trySend(snapshot.toObjects(FreedomPost::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    // --- Messaging ---
    suspend fun sendMessage(msg: Message): Result<Unit> {
        return try {
            val ref = db.collection("messages").document()
            ref.set(msg.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getChatSummaries(): Flow<List<ChatSummary>> = callbackFlow {
        // For a prototype, we'll return a static list or a simple query.
        // In a real app, this would be a complex query of 'chats' where the user is a participant.
        trySend(emptyList()) 
        awaitClose { }
    }
}
