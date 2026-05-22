package com.example.westcon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.westcon.ui.theme.*

import java.text.SimpleDateFormat
import java.util.*

import com.example.westcon.ui.UIUtils
import com.example.westcon.ui.WestconPullToRefresh
import kotlinx.coroutines.launch

@Composable
fun MessageScreen(onMessageClick: (String, String) -> Unit = { _, _ -> }) {
    val chatSummaries by com.example.westcon.data.FirebaseManager.getChatSummaries().collectAsState(initial = emptyList())
    var searchText by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    val filteredSummaries = remember(chatSummaries, searchText) {
        if (searchText.isBlank()) chatSummaries
        else chatSummaries.filter { it.otherUserName.contains(searchText, ignoreCase = true) }
    }

    WestconPullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
                kotlinx.coroutines.delay(1500)
                isRefreshing = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)) // Subtle off-white background
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(WestconDarkBlue, Color(0xFF002244))
                        )
                    )
                    .padding(top = 20.dp, bottom = 28.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        "Connect with WestCon skilled learners",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = MomotrustFontFamily
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    MessageSearchBar(
                        value = searchText,
                        onValueChange = { searchText = it }
                    )
                }
            }
            
            if (chatSummaries.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No conversations yet", color = Color.Gray, fontFamily = MomotrustFontFamily)
                        Text("Start a skill exchange to chat!", fontSize = 12.sp, color = Color.LightGray, fontFamily = MomotrustFontFamily)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
                ) {
                    items(filteredSummaries) { summary ->
                        MessageItem(summary, onClick = {
                            val currentUid = com.example.westcon.data.FirebaseManager.getCurrentUser()?.uid ?: ""
                            val chatId = if (currentUid < summary.otherUserUid) "${currentUid}_${summary.otherUserUid}" else "${summary.otherUserUid}_$currentUid"
                            onMessageClick(chatId, summary.otherUserName)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MessageSearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Search chats...", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = WestconYellow, modifier = Modifier.size(20.dp))
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            focusedContainerColor = Color.White.copy(alpha = 0.15f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedBorderColor = WestconYellow,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun MessageItem(summary: com.example.westcon.data.ChatSummary, onClick: () -> Unit) {
    val timeStr = formatTimestamp(summary.timestamp)
    val departmentColor = when(summary.otherUserDept) {
        "CAS" -> WestconYellow.copy(alpha = 0.2f)
        "CICT" -> Color(0xFFE3F2FD)
        "CON" -> Color(0xFFE8F5E9)
        "COE" -> Color(0xFFFFF3E0)
        else -> Color(0xFFE9ECEF)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with Online Status Indicator
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F3F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        UIUtils.getProfileIcon(summary.otherUserIconName), 
                        contentDescription = null, 
                        modifier = Modifier.size(36.dp),
                        tint = WestconDarkBlue
                    )
                }
                
                // Status dot (always green for demo/simulation feel)
                Surface(
                    color = Color(0xFF4CAF50),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(14.dp)
                        .border(2.dp, Color.White, CircleShape)
                ) {}
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        summary.otherUserName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = WestconDarkBlue,
                        fontFamily = MomotrustFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = departmentColor,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            summary.otherUserDept,
                            color = if (departmentColor == WestconYellow.copy(alpha = 0.2f)) Color(0xFF8B7355) else WestconDarkBlue.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                if (summary.isTyping) {
                    Text(
                        "is typing...",
                        color = Color(0xFF4CAF50),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    Text(
                        summary.lastMessage,
                        color = if (summary.unreadCount > 0) Color.DarkGray else Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = if (summary.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                Text(timeStr, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                if (summary.unreadCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = WestconYellow,
                        shape = CircleShape,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                summary.unreadCount.toString(),
                                color = WestconDarkBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                } else {
                    // Check icon to show message was read/delivered
                    Spacer(modifier = Modifier.height(6.dp))
                    Icon(
                        Icons.Default.DoneAll, 
                        contentDescription = null, 
                        tint = Color.LightGray, 
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
    val now = Calendar.getInstance()
    val time = Calendar.getInstance().apply { time = timestamp.toDate() }
    
    return when {
        now.get(Calendar.DATE) == time.get(Calendar.DATE) -> 
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp.toDate())
        now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1 -> "Yesterday"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(timestamp.toDate())
    }
}
