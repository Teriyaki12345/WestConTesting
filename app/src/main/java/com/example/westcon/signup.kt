package com.example.westcon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterScreen(onJoinClick: () -> Unit, onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .padding(top = 110.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = null,
                tint = WestconYellow,
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(height = 20.dp))
            Text(
                text = "Welcome,\nTaga-WEST!",
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .offset(y = (-30).dp),
                color = Color.White,
                fontSize = 42.sp,
                lineHeight = 46.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MomotrustFontFamily
            )

            Spacer(modifier = Modifier.height(0.dp))

            // Fields
            SignUpTextField(label = "Student ID", icon = R.drawable.tdesign_education_filled)
            SignUpTextField(label = "WVSU email", icon = R.drawable.email)

            // Password fields with the Eye Icon logic
            SignUpTextField(
                label = "Password",
                icon = R.drawable.lock,
                isPassword = true,
                showEyeIcon = true // Enable the eye toggle here
            )
            SignUpTextField(
                label = "Re-enter password",
                icon = R.drawable.lock,
                isPassword = true,
                showEyeIcon = false // Eye icon stays hidden here
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001229))
            ) {
                Text("Join WESTCON", color = WestconYellow, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = MomotrustFontFamily)
            }

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Go Back", color = Color.White.copy(alpha = 0.7f), fontFamily = MomotrustFontFamily)
            }
        }
        FooterSection(Modifier.align(Alignment.BottomCenter))
    }
}


