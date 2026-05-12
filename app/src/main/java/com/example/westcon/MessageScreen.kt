package com.example.westcon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MessageSearchBar()
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(sampleMessages) { message ->
                MessageItem(message)
            }
        }
    }
}

@Composable
fun MessageSearchBar() {
    var searchText by remember { mutableStateOf("") }
    
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Search conversations", color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F3F5),
            focusedContainerColor = Color(0xFFF1F3F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}

data class ChatMessage(
    val name: String,
    val department: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isTyping: Boolean = false,
    val departmentColor: Color = WestconYellow.copy(alpha = 0.2f)
)

val sampleMessages = listOf(
    ChatMessage("Maria Clara", "CAS", "Is the tutorial for React still available?", "10:45 AM", unreadCount = 2, isTyping = true),
    ChatMessage("Juan Dela Cruz", "CICT", "Thanks for the notes on Advanced Math! They really helped.", "Yesterday", departmentColor = Color(0xFFE3F2FD)),
    ChatMessage("Elena Reyes", "CON", "Can we meet at the library to swap the textbooks tomorrow morning?", "Tue"),
    ChatMessage("Professor Santos", "FACULTY", "The exchange proposal has been approved for the student lounge.", "Mon", departmentColor = Color(0xFFE9ECEF)),
    ChatMessage("Grace Lee", "COE", "I've sent the link for the project assets.", "Nov 12")
)

@Composable
fun MessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with Online Status Indicator (as seen in screenshot for first item)
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
            }
            if (message.isTyping) { // Simple heuristic to match the "online" dot in screenshot
                Surface(
                    color = Color(0xFF4CAF50),
                    shape = CircleShape,
                    modifier = Modifier.size(12.dp).border(2.dp, Color.White, CircleShape)
                ) {}
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    message.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = WestconDarkBlue,
                    fontFamily = MomotrustFontFamily
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = message.departmentColor,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        message.department,
                        color = if (message.departmentColor == WestconYellow.copy(alpha = 0.2f)) Color(0xFF856404) else WestconDarkBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            if (message.isTyping) {
                Text(
                    "Typing...",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            } else {
                Text(
                    message.lastMessage,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(message.time, color = Color.Gray, fontSize = 11.sp)
            if (message.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = WestconDarkBlue,
                    shape = CircleShape,
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            message.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
