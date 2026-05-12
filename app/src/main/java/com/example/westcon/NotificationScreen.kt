package com.example.westcon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = WestconDarkBlue, fontSize = 18.sp, fontFamily = MomotrustFontFamily) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WestconDarkBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Divider(color = Color(0xFFF1F3F5), thickness = 1.dp)
            NotificationFilters()
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    NotificationCard(
                        title = "Skill Exchange",
                        timeAgo = "2m ago",
                        icon = Icons.Default.Person, // Fallback for avatar
                        iconBgColor = Color.LightGray,
                        iconContentColor = Color.Gray,
                        isAvatar = true
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Juan Dela Cruz ")
                                }
                                append("wants to exchange ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("'React Basics' ")
                                }
                                append("for your ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("'UI/UX Logic'")
                                }
                                append(".")
                            },
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f).height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Accept", color = Color.White)
                            }
                            OutlinedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f).height(40.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Decline", color = Color.DarkGray)
                            }
                        }
                    }
                }
                
                item {
                    NotificationCard(
                        title = "Achievement",
                        timeAgo = "1h ago",
                        icon = Icons.Default.Star,
                        iconBgColor = WestconYellow,
                        iconContentColor = WestconDarkBlue
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("Badge Earned! You are now a ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = WestconDarkBlue)) {
                                    append("'SQL Master'")
                                }
                                append(".\nKeep up the great work, Taga-West!")
                            },
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
                
                item {
                    NotificationCard(
                        title = "Message",
                        timeAgo = "Yesterday",
                        icon = Icons.Default.Person, // Fallback for avatar
                        iconBgColor = Color.LightGray,
                        iconContentColor = Color.Gray,
                        isAvatar = true,
                        showArrow = true
                    ) {
                        Text(
                            buildAnnotatedString {
                                append("New message from ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Maria Clara")
                                }
                                append(".")
                            },
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "\"Hi! I saw your post about 'UI/UX Logic'...\"",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = Color(0xFFE9ECEF))
                        Text(
                            "YESTERDAY",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(modifier = Modifier.weight(1f), color = Color(0xFFE9ECEF))
                    }
                }
                
                item {
                    NotificationCard(
                        title = "Weekly Digest",
                        timeAgo = "1d ago",
                        icon = Icons.Default.AccessTime,
                        iconBgColor = Color(0xFFE9ECEF),
                        iconContentColor = Color.Gray,
                        containerColor = Color(0xFFF8F9FA) // Grayed out background
                    ) {
                        Text(
                            "You helped 3 students last week! View your impact report.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationFilters() {
    var selectedFilter by remember { mutableStateOf("All") }
    
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("All", "Requests").forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { selectedFilter = filter },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WestconDarkBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.DarkGray
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == filter,
                    borderColor = Color.LightGray,
                    selectedBorderColor = WestconDarkBlue
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun NotificationCard(
    title: String,
    timeAgo: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconContentColor: Color,
    isAvatar: Boolean = false,
    showArrow: Boolean = false,
    containerColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F3F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon or Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBgColor)
                    .then(if (isAvatar) Modifier.border(2.dp, WestconYellow, CircleShape) else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconContentColor, modifier = Modifier.size(if(isAvatar) 24.dp else 20.dp))
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        color = if (title == "Achievement") Color(0xFF856404) else WestconDarkBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = MomotrustFontFamily
                    )
                    Text(timeAgo, color = Color.Gray, fontSize = 10.sp)
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                content()
            }
            
            if (showArrow) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = WestconDarkBlue, modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }
}
