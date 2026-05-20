package com.example.westcon.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.*

object UIUtils {
    fun getProfileIcon(name: String): ImageVector {
        return when (name) {
            "Person" -> Icons.Default.Person
            "Face" -> Icons.Default.Face
            "School" -> Icons.Default.School
            "Psychology" -> Icons.Default.Psychology
            "Engineering" -> Icons.Default.Engineering
            "Computer" -> Icons.Default.Computer
            "Brush" -> Icons.Default.Brush
            "MenuBook" -> Icons.Default.MenuBook
            "Science" -> Icons.Default.Science
            "Palette" -> Icons.Default.Palette
            "SportsEsports" -> Icons.Default.SportsEsports
            "MusicNote" -> Icons.Default.MusicNote
            "FitnessCenter" -> Icons.Default.FitnessCenter
            "LocalFlorist" -> Icons.Default.LocalFlorist
            "AutoAwesome" -> Icons.Default.AutoAwesome
            "RocketLaunch" -> Icons.Default.RocketLaunch
            "Terminal" -> Icons.Default.Terminal
            "Calculate" -> Icons.Default.Calculate
            "AutoStories" -> Icons.Default.AutoStories
            "Public" -> Icons.Default.Public
            "Language" -> Icons.Default.Language
            "Draw" -> Icons.Default.Draw
            "TheaterComedy" -> Icons.Default.TheaterComedy
            "MicExternalOn" -> Icons.Default.MicExternalOn
            "Photography" -> Icons.Default.PhotoCamera
            "Camera" -> Icons.Default.Camera
            "DirectionsRun" -> Icons.Default.DirectionsRun
            "SportsBasketball" -> Icons.Default.SportsBasketball
            "SportsSoccer" -> Icons.Default.SportsSoccer
            "Work" -> Icons.Default.Work
            "Lightbulb" -> Icons.Default.Lightbulb
            "Medication" -> Icons.Default.Medication
            "AccountBalance" -> Icons.Default.AccountBalance
            else -> Icons.Default.Person
        }
    }

    val availableIcons = listOf(
        "Person", "Face", "School", "Psychology", "Engineering", 
        "Computer", "Brush", "MenuBook", "Science", "Palette", 
        "SportsEsports", "MusicNote", "FitnessCenter", "LocalFlorist", 
        "AutoAwesome", "RocketLaunch", "Terminal", "Calculate",
        "AutoStories", "Public", "Language", "Draw", "TheaterComedy",
        "MicExternalOn", "Photography", "Camera", "DirectionsRun",
        "SportsBasketball", "SportsSoccer", "Work", "Lightbulb",
        "Medication", "AccountBalance"
    )

    fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
        val now = Calendar.getInstance().timeInMillis
        val diff = now - timestamp.toDate().time

        return when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000}m ago"
            diff < 86400000 -> "${diff / 3600000}h ago"
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(timestamp.toDate())
        }
    }

    val freedomWallColors = listOf(
        "#E3F2FD", // Light Blue
        "#F3E5F5", // Light Purple
        "#E8F5E9", // Light Green
        "#FFF3E0", // Light Orange
        "#FCE4EC", // Light Pink
        "#F1F8E9", // Light Lime
        "#FFFDE7", // Light Yellow
        "#E0F7FA", // Light Cyan
        "#FBE9E7"  // Light Deep Orange
    )
}
