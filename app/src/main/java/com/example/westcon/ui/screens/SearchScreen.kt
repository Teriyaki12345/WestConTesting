package com.example.westcon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.westcon.ui.theme.*
import com.example.westcon.data.FirebaseManager
import com.example.westcon.data.SkillPost
import com.example.westcon.ui.UIUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBackClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val posts by FirebaseManager.getSkillPosts().collectAsState(initial = emptyList())
    
    val filteredPosts = remember(searchQuery, posts) {
        if (searchQuery.isBlank()) emptyList()
        else posts.filter { 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.description.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WestconDarkBlue)
                        }
                        
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Find skills, departments...", color = Color.Gray, fontSize = 15.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF1F5F9)),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = WestconDarkBlue,
                                unfocusedTextColor = WestconDarkBlue,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = WestconDarkBlue
                            ),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Outlined.Search, contentDescription = null, tint = WestconDarkBlue)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Clear", tint = Color.Gray)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
        ) {
            if (searchQuery.isEmpty()) {
                InitialSearchState(onCategoryClick = { searchQuery = it })
            } else {
                SearchResultsContent(searchQuery, filteredPosts)
            }
        }
    }
}

@Composable
fun InitialSearchState(onCategoryClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "Popular Categories",
                    color = WestconDarkBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MomotrustFontFamily
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                val categories = listOf("Technology", "Academics", "Arts", "Language", "Sports", "Others")
                val categoryIcons = listOf(
                    Icons.Default.Computer, 
                    Icons.Default.School, 
                    Icons.Default.Palette, 
                    Icons.Default.Language, 
                    Icons.Default.SportsBasketball,
                    Icons.Default.AutoAwesome
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    categories.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { category ->
                                CategorySearchCard(
                                    label = category,
                                    icon = categoryIcons[categories.indexOf(category)],
                                    onClick = { onCategoryClick(category) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(32.dp)) }
        
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recent Searches",
                        color = WestconDarkBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MomotrustFontFamily
                    )
                    TextButton(onClick = { /* Clear History */ }) {
                        Text("Clear", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                val recentItems = listOf("UI/UX Design", "Kotlin Coroutines", "Digital Arts", "Calculus 1")
                recentItems.forEach { item ->
                    RecentSearchItem(item, onClick = { onCategoryClick(item) })
                }
            }
        }
    }
}

@Composable
fun CategorySearchCard(
    label: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(WestconYellow.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = WestconYellow, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = WestconDarkBlue
            )
        }
    }
}

@Composable
fun RecentSearchItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.History, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = Color.DarkGray, fontSize = 15.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
    }
}

@Composable
fun SearchResultsContent(query: String, results: List<SkillPost>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Results for \"$query\"",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Surface(
                color = WestconDarkBlue.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    "${results.size} found",
                    color = WestconDarkBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        
        if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.SearchOff, 
                            contentDescription = null, 
                            modifier = Modifier.size(60.dp), 
                            tint = Color.LightGray
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "No results found",
                        color = WestconDarkBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MomotrustFontFamily
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "We couldn't find any skills matching \"$query\". Try checking your spelling or use different keywords.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(results, key = { it.id }) { post ->
                    SkillPostCard(
                        post = post,
                        isOwnPost = post.authorUid == FirebaseManager.getCurrentUser()?.uid,
                        onExchangeClick = { /* Handle in Dashboard or separate state */ },
                        onMessageClick = { /* Handle message */ }
                    )
                }
            }
        }
    }
}
