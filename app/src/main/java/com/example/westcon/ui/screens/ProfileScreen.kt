package com.example.westcon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { ProfileHeaderCard() }
        item { AboutSection() }
        item { SkillsCanTeachSection() }
        item { SkillsLearningSection() }
    }
}

@Composable
fun ProfileHeaderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with Yellow Border
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, WestconYellow, CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Gray)
                }
                // Verification Badge
                Surface(
                    color = WestconDarkBlue,
                    shape = CircleShape,
                    modifier = Modifier.size(28.dp).border(2.dp, Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Verified", tint = Color.White, modifier = Modifier.padding(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Jaspher John Ebarle",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WestconDarkBlue,
                fontFamily = MomotrustFontFamily
            )

            Text(
                "COLLEGE OF ICT | BS COMPUTER SCIENCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B7355), // Brownish gold color from screenshot
                fontFamily = MomotrustFontFamily,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileStatBadge(Icons.Default.History, "Year 3")
                ProfileStatBadge(Icons.Default.Star, "4.9 Rating")
                ProfileStatBadge(Icons.Default.SwapHoriz, "12 Swaps")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Message", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WestconDarkBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Exchange Skills", color = WestconDarkBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileStatBadge(icon: ImageVector, text: String) {
    Surface(
        color = Color(0xFFF1F3F5),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("About", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = WestconDarkBlue)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Passionate tech enthusiast focused on full-stack development and data structures. I enjoy breaking down complex coding concepts into digestible bits for my peers. Looking to expand my creative horizon through digital design and photography.",
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsCanTeachSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = WestconDarkBlue),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = WestconYellow, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Skills I Can Teach", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("React & Next.js", "Python Basics", "Academic Writing", "UI/UX Logic").forEach { skill ->
                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            skill,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable FlowRowScope.() -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
fun SkillsLearningSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF856404), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Skills I'm Learning", fontWeight = FontWeight.Bold, color = WestconDarkBlue, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            SkillProgressBar("Digital Illustration", 0.45f)
            SkillProgressBar("Digital Illustration", 0.45f)
            SkillProgressBar("Digital Illustration", 0.45f)
        }
    }
}

@Composable
fun SkillProgressBar(skill: String, progress: Float) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(skill, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF856404))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = WestconYellow,
            trackColor = Color(0xFFE9ECEF)
        )
    }
}
