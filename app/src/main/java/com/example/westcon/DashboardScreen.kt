package com.example.westcon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showNotifications by remember { mutableStateOf(false) }

    if (showNotifications) {
        NotificationScreen(onBackClick = { showNotifications = false })
    } else {
        Scaffold(
            topBar = { DashboardTopBar(onNotificationClick = { showNotifications = true }) },
            bottomBar = { DashboardBottomNav(selectedTab) { selectedTab = it } },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* TODO */ },
                    containerColor = WestconDarkBlue,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Post")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    0 -> HomeFeed()
                    1 -> FreedomWallScreen()
                    2 -> MessageScreen()
                    3 -> ProfileScreen()
                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Screen $selectedTab Coming Soon", fontFamily = MomotrustFontFamily)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeFeed() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        item { PostSkillCard() }
        item { CategoryChips() }
        items(samplePosts) { post ->
            SkillPostCard(post)
        }
    }
}

@Composable
fun DashboardTopBar(onNotificationClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // User Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "WestCon",
                color = WestconDarkBlue,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MomotrustFontFamily
            )
        }
        IconButton(onClick = onNotificationClick) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = WestconDarkBlue,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun PostSkillCard() {
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
                        .clip(CircleShape)
                        .background(Color(0xFFE9ECEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = WestconDarkBlue, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "What skill can you share today?",
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
                    Text("Post Skill", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CategoryChips() {
    val categories = listOf("All Skills", "Technology", "Academics", "Arts", "Language")
    var selectedCategory by remember { mutableStateOf("All Skills") }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { selectedCategory = category },
                label = { Text(category, fontFamily = MomotrustFontFamily) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WestconDarkBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Gray
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedCategory == category,
                    borderColor = Color.LightGray,
                    selectedBorderColor = WestconDarkBlue
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

data class Post(
    val author: String,
    val department: String,
    val timeAgo: String,
    val category: String,
    val title: String,
    val description: String,
    val isAnonymous: Boolean = false
)

val samplePosts = listOf(
    Post(
        "Maria Clara", "CAS", "2h ago", "Technology",
        "Teaching Python Basics",
        "I can help you get started with Python programming! We'll cover variables, loops, and basic functions. Perfect for CCIS students struggling with Intro to Computing."
    ),
    Post(
        "Anonymous Student", "CON", "5h ago", "Academics",
        "Anatomy Mnemonics & Review",
        "Offering review sessions for Anatomy and Physiology. I have a collection of mnemonics that helped me ace my prelims. Looking to exchange for Statistics help!",
        true
    )
)

@Composable
fun SkillPostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (post.isAnonymous) WestconDarkBlue else Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (post.isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(post.author, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${post.department} • ${post.timeAgo}", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                
                Surface(
                    color = WestconYellow.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        post.category,
                        color = Color(0xFF856404),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WestconDarkBlue,
                fontFamily = MomotrustFontFamily
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                post.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Exchange", fontSize = 14.sp)
                }
                
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(0.4f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9ECEF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Message", color = WestconDarkBlue, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun DashboardBottomNav(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("HOME", Icons.Default.Home, 0),
            Triple("FREEDOM WALL", Icons.Default.EditNote, 1),
            Triple("MESSAGES", Icons.Default.Email, 2),
            Triple("PROFILE", Icons.Default.Person, 3)
        )
        
        items.forEach { (label, icon, index) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WestconDarkBlue,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = WestconDarkBlue,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
