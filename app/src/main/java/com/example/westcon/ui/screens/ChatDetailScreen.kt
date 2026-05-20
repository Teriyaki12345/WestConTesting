package com.example.westcon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.westcon.ui.theme.*
import com.example.westcon.data.FirebaseManager
import com.example.westcon.data.Message
import com.example.westcon.data.UserProfile
import com.example.westcon.ui.UIUtils
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatId: String,
    otherUserName: String,
    onBackClick: () -> Unit
) {
    val messages by FirebaseManager.getMessages(chatId).collectAsState(initial = emptyList())
    val currentUser = FirebaseManager.getCurrentUser()
    var otherUserProfile by remember { mutableStateOf<UserProfile?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }
    var showRateDialog by remember { mutableStateOf(false) }

    // Fetch other user's profile for the icon
    LaunchedEffect(chatId) {
        val currentUid = currentUser?.uid ?: ""
        val otherUid = chatId.split("_").find { it != currentUid } ?: ""
        if (otherUid.isNotEmpty()) {
            otherUserProfile = FirebaseManager.getUserProfile(otherUid)
        }
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(WestconDarkBlue, Color(0xFF002244))
                        )
                    )
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            UIUtils.getProfileIcon(otherUserProfile?.profileIconName ?: "Person"), 
                            contentDescription = null, 
                            tint = WestconYellow, 
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            otherUserName, 
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color.White, 
                            fontFamily = MomotrustFontFamily
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Online", 
                                fontSize = 11.sp, 
                                color = Color.White.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    IconButton(onClick = { showRateDialog = true }) {
                        Icon(Icons.Outlined.Star, contentDescription = "Rate", tint = WestconYellow)
                    }
                }
            }
        },
        bottomBar = {
            ChatInputBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank() && currentUser != null) {
                        val newMessage = Message(
                            senderUid = currentUser.uid,
                            receiverUid = "", // Handled by FirebaseManager logic
                            text = messageText,
                            timestamp = Timestamp.now()
                        )
                        scope.launch {
                            FirebaseManager.sendMessage(newMessage, chatId)
                            messageText = ""
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF1F3F5)) // WhatsApp-style subtle background
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message, isCurrentUser = message.senderUid == currentUser?.uid)
                }
            }
        }
    }

    if (showRateDialog) {
        RateUserDialog(
            otherUserName = otherUserName,
            onDismiss = { showRateDialog = false },
            onRate = { rating, skillName ->
                scope.launch {
                    val currentUid = currentUser?.uid ?: ""
                    val otherUid = chatId.split("_").find { it != currentUid } ?: ""
                    if (otherUid.isNotEmpty()) {
                        FirebaseManager.rateUserSkill(otherUid, skillName, rating)
                    }
                    showRateDialog = false
                }
            }
        )
    }
}

@Composable
fun ChatBubble(message: Message, isCurrentUser: Boolean) {
    val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(message.timestamp.toDate())
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isCurrentUser) WestconDarkBlue else Color.White,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isCurrentUser) 20.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 20.dp
            ),
            tonalElevation = if (isCurrentUser) 2.dp else 1.dp,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    message.text,
                    color = if (isCurrentUser) Color.White else Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    timeStr,
                    color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 12.dp,
        modifier = Modifier.navigationBarsPadding().imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expressive icons for modern feel
            IconButton(onClick = { /* Add emoji/attachment logic here if needed */ }) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Add", tint = Color.Gray)
            }
            
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                placeholder = { Text("Write a message...", fontSize = 15.sp, color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF1F3F5),
                    focusedContainerColor = Color(0xFFF1F3F5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = WestconYellow.copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 5,
                singleLine = false
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = onSendClick,
                enabled = messageText.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (messageText.isNotBlank()) WestconDarkBlue else Color.LightGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Send, 
                    contentDescription = "Send", 
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun RateUserDialog(
    otherUserName: String,
    onDismiss: () -> Unit,
    onRate: (Double, String) -> Unit
) {
    var rating by remember { mutableDoubleStateOf(5.0) }
    var skillName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Rate $otherUserName's Teaching", 
                fontWeight = FontWeight.Bold, 
                color = WestconDarkBlue,
                fontFamily = MomotrustFontFamily
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("How would you rate the quality of the teaching session?", fontSize = 14.sp, color = Color.Gray)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        val isFilled = index < rating.toInt()
                        IconButton(onClick = { rating = (index + 1).toDouble() }) {
                            Icon(
                                if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = WestconYellow,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                
                Text(
                    "${String.format("%.1f", rating)} Stars",
                    fontWeight = FontWeight.Black,
                    color = WestconDarkBlue,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = skillName,
                    onValueChange = { skillName = it },
                    label = { Text("What skill did you learn?") },
                    placeholder = { Text("e.g. Kotlin, UI/UX, Guitar") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (skillName.isNotBlank()) onRate(rating, skillName) },
                enabled = skillName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Submit Rating", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { 
                Text("Later", color = Color.Gray) 
            }
        }
    )
}
