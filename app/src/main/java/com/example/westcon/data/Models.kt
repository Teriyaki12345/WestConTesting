package com.example.westcon.data

import com.google.firebase.Timestamp

data class SkillMastery(
    val skillName: String = "",
    val averageRating: Double = 0.0,
    val totalRatings: Int = 0,
    val level: Int = 1 // 1: Novice, 2: Intermediate, 3: Advanced, 4: Expert, 5: Guru
)

// Firestore collection: users
// Document ID: user's uid
// This stores the authenticated user's profile and preferences.
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileIconName: String = "Person", // Default icon
    val department: String = "",
    val course: String = "",
    val year: String = "",
    val rating: Double = 0.0,
    val swaps: Int = 0,
    val about: String = "",
    val skillsToTeach: List<SkillMastery> = emptyList(),
    val skillsLearning: Map<String, Int> = emptyMap() // Skill Name to Progress Percentage
)

data class SkillPost(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "",
    val authorIconName: String = "Person",
    val authorMastery: Int = 1, // Proficiency level of the author in this skill
    val department: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    // Firestore documents may use `anonymous` (no `is` prefix) or `isAnonymous`.
    var anonymous: Boolean = false,
    val timestamp: Timestamp = Timestamp.now()
)

data class FreedomPost(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "User",
    val authorIconName: String = "Person",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(), // List of UIDs
    val colorHex: String = "#E3F2FD",
    val topComment: String? = null,
    val commentCount: Int = 0,
    // keep Firestore-friendly field
    var anonymous: Boolean = true
)

data class Message(
    val id: String = "",
    val senderUid: String = "",
    val receiverUid: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    // Firestore may store boolean fields as either "read" or "isRead" depending on older clients.
    // Use a mutable `read` field for Firestore mapping.
    var read: Boolean = false
)

data class ChatSummary(
    val otherUserUid: String = "",
    val otherUserName: String = "",
    val otherUserIconName: String = "Person",
    val otherUserDept: String = "",
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val unreadCount: Int = 0,
    val lastMessageSenderUid: String = "",
    var lastMessageRead: Boolean = true,
    // Firestore documents might have a `typing` field (no `is` prefix). Keep a mutable `typing` field
    // for mapping.
    var typing: Boolean = false
)

data class Notification(
    val id: String = "",
    val receiverUid: String = "",
    val type: String = "SKILL_EXCHANGE", // SKILL_EXCHANGE, ACHIEVEMENT, MESSAGE, DIGEST
    val title: String = "",
    val content: String = "",
    val senderUid: String? = null,
    val senderName: String? = null,
    val senderIconName: String = "Person",
    val senderDept: String? = null,
    val skillOffered: String? = null,
    val skillWanted: String? = null,
    val timestamp: Timestamp = Timestamp.now(),
    // Firestore historically may store this field as `read` or `isRead`.
    // Use a mutable `read` so Firestore mapping can bind it.
    var read: Boolean = false
)

val SkillPost.isAnonymous: Boolean get() = anonymous
val FreedomPost.isAnonymous: Boolean get() = anonymous
val Message.isRead: Boolean get() = read
val Notification.isRead: Boolean get() = read
val ChatSummary.isTyping: Boolean get() = typing
