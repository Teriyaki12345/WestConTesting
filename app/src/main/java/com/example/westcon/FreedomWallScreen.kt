package com.example.westcon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FreedomWallScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        PostFreedomWallCard()
        FreedomWallFilters()
        
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp
        ) {
            items(freedomWallPosts) { post ->
                FreedomPostCard(post)
            }
        }
    }
}

@Composable
fun PostFreedomWallCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE9ECEF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = WestconDarkBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "What's on your mind?",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = MomotrustFontFamily
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var isAnonymous by remember { mutableStateOf(false) }
                    Switch(
                        checked = isAnonymous,
                        onCheckedChange = { isAnonymous = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = WestconDarkBlue
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Post Anonymously", fontSize = 12.sp, fontFamily = MomotrustFontFamily)
                }
                
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Text("Post", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FreedomWallFilters() {
    val filters = listOf("All Posts", "Help som1 smthng option", "Tips & Advice")
    var selectedFilter by remember { mutableStateOf("All Posts") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { selectedFilter = filter },
                label = { Text(filter, fontSize = 12.sp, fontFamily = MomotrustFontFamily) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WestconDarkBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Gray
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

data class FreedomPost(
    val content: String,
    val timeAgo: String,
    val likes: Int,
    val color: Color,
    val topComment: String? = null,
    val commentCount: Int? = null,
    val showShare: Boolean = false
)

val freedomWallPosts = listOf(
    FreedomPost(
        "\"Good luck on finals, CAS students! We can do this! Fighting! 🎓✨\"",
        "2m ago", 24, Color(0xFFE3F2FD) // Light Blue
    ),
    FreedomPost(
        "\"To the person in the library with the blue hoodie... your focus is inspiring. 💙✨\"",
        "15m ago", 8, Color(0xFFFFF9C4) // Light Yellow
    ),
    FreedomPost(
        "\"Can we have more vegan options in the canteen? Even just simple vegetable dishes would be great for us plant-based students. 🥦✨\"",
        "1h ago", 42, Color(0xFFE8F5E9), // Light Green
        topComment = "Agreed! Maybe some tofu dishes?",
        commentCount = 12
    ),
    FreedomPost(
        "\"Is it just me or was that Chem exam surprisingly hard today? I'm crying inside. 😭✨\"",
        "3h ago", 56, Color(0xFFFCE4EC) // Light Pink
    ),
    FreedomPost(
        "\"Coffee at 11 PM: Good for productivity, bad for my 7 AM class. Send help. ☕💤\"",
        "8h ago", 19, Color(0xFFF3E5F5) // Light Purple
    ),
    FreedomPost(
        "\"Lost a white AirPods case at the football field around 4 PM. If found, please leave it at the gate guard! 🎧✨\"",
        "10h ago", 0, Color(0xFFE0F2F1), // Teal
        showShare = true
    )
)

@Composable
fun FreedomPostCard(post: FreedomPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = post.color),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                post.content,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic
            )
            
            if (post.topComment != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Top Comment: \"${post.topComment}\"", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(post.timeAgo, fontSize = 10.sp, color = Color.Gray)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.showShare) {
                        Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    } else {
                        if (post.commentCount != null) {
                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.commentCount}", fontSize = 10.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(14.dp), tint = WestconDarkBlue)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${post.likes}", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
