package com.example.westcon.ui.screens

import com.example.westcon.R
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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onBackClick: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
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
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Yellow Cap Icon
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = null,
                tint = WestconYellow,
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Title Text
            Text(
                text = "Welcome Back,\nTaga-WEST!",
                color = Color.White,
                fontSize = 42.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MomotrustFontFamily,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Text Fields
            SignUpTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = R.drawable.email
            )

            SignUpTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = R.drawable.lock,
                isPassword = true,
                showEyeIcon = true // Adds the eye icon from your secret.xml
            )

            // Forgot Password Link
            TextButton(
                onClick = { /* Handle Forgot Password */ },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forgot password?",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = MomotrustFontFamily,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Enter Button
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Please fill all fields"
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        val result = com.example.westcon.data.FirebaseManager.login(email, password)
                        isLoading = false
                        if (result.isSuccess) {
                            onLoginSuccess()
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001D3D)) // Dark Navy
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = WestconYellow, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Enter WESTCON",
                        color = WestconYellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MomotrustFontFamily
                    )
                }
            }

            // Go Back Button (To return to Landing)
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Go Back",
                    color = Color.White.copy(alpha = 0.6f),
                    fontFamily = MomotrustFontFamily
                )
            }
        }

        // Shared Footer
        FooterSection(Modifier.align(Alignment.BottomCenter))
    }
}