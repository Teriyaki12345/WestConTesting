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
import androidx.compose.ui.text.style.TextAlign
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
                        val currentUid = currentUser.uid
                        val otherUid = chatId.split("_").find { it != currentUid } ?: ""
                        
                        android.util.Log.d("ChatDetailScreen", "Sending message - currentUid: $currentUid, otherUid: $otherUid, chatId: $chatId")
                        
                        if (otherUid.isNotEmpty()) {
                            val newMessage = Message(
                                senderUid = currentUid,
                                receiverUid = otherUid,
                                text = messageText,
                                timestamp = Timestamp.now()
                            )
                            scope.launch {
                                val result = FirebaseManager.sendMessage(newMessage, chatId)
                                if (result.isSuccess) {
                                    android.util.Log.d("ChatDetailScreen", "Message sent successfully")
                                    messageText = ""
                                } else {
                                    android.util.Log.e("ChatDetailScreen", "Failed to send message: ${result.exceptionOrNull()?.message}")
                                }
                            }
                        } else {
                            android.util.Log.e("ChatDetailScreen", "Failed to extract otherUid from chatId: $chatId")
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
        modifier = Modifier.padding(WindowInsets.ime.asPaddingValues())
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
    
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(WestconYellow.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Stars,
                        contentDescription = null,
                        tint = WestconYellow,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Rate $otherUserName's Teaching",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WestconDarkBlue,
                    fontFamily = MomotrustFontFamily,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    "How would you rate the quality of the teaching session?",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Star Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        val isSelected = index < rating.toInt()
                        IconButton(
                            onClick = { rating = (index + 1).toDouble() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = if (isSelected) WestconYellow else Color.LightGray.copy(alpha = 0.5f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    when(rating.toInt()) {
                        1 -> "Poor"
                        2 -> "Fair"
                        3 -> "Good"
                        4 -> "Very Good"
                        5 -> "Excellent!"
                        else -> "Excellent!"
                    },
                    fontWeight = FontWeight.ExtraBold,
                    color = WestconDarkBlue,
                    fontSize = 16.sp,
                    fontFamily = MomotrustFontFamily
                )
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Skill Input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "What skill did you learn?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = WestconDarkBlue,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = skillName,
                        onValueChange = { skillName = it },
                        placeholder = { Text("e.g. Kotlin, UI/UX, Guitar", color = Color.Gray.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = WestconDarkBlue,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Later", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = { if (skillName.isNotBlank()) onRate(rating, skillName) },
                        enabled = skillName.isNotBlank(),
                        modifier = Modifier.weight(1.5f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WestconDarkBlue,
                            disabledContainerColor = Color.LightGray.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text("Submit Rating", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
