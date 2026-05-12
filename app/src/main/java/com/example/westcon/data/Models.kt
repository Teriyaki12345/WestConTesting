package com.example.westcon.data

import com.google.firebase.Timestamp

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val department: String = "",
    val course: String = "",
    val year: String = "",
    val rating: Double = 5.0,
    val swaps: Int = 0,
    val about: String = "",
    val skillsToTeach: List<String> = emptyList(),
    val skillsLearning: Map<String, Int> = emptyMap() // Skill Name to Progress Percentage
)

data class SkillPost(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "",
    val department: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val isAnonymous: Boolean = false,
    val timestamp: Timestamp = Timestamp.now()
)

data class FreedomPost(
    val id: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(), // List of UIDs
    val colorHex: String = "#E3F2FD",
    val topComment: String? = null,
    val commentCount: Int = 0,
    val isAnonymous: Boolean = true
)

data class Message(
    val id: String = "",
    val senderUid: String = "",
    val receiverUid: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false
)

data class ChatSummary(
    val otherUserUid: String = "",
    val otherUserName: String = "",
    val otherUserDept: String = "",
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val unreadCount: Int = 0,
    val isTyping: Boolean = false
)
