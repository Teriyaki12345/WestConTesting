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
import com.example.westcon.ui.theme.*
import kotlinx.coroutines.launch
import com.example.westcon.data.FirebaseManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterScreen(onJoinClick: (String, String) -> Unit, onBackClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }

            Spacer(modifier = Modifier.height(0.dp))

            // Fields
            SignUpTextField(
                value = email,
                onValueChange = { email = it },
                label = "WVSU email",
                icon = R.drawable.email
            )

            // Password fields with the Eye Icon logic
            SignUpTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = R.drawable.lock,
                isPassword = true,
                showEyeIcon = true // Enable the eye toggle here
            )
            SignUpTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Re-enter password",
                icon = R.drawable.lock,
                isPassword = true,
                showEyeIcon = false // Eye icon stays hidden here
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Please fill all fields"
                        return@Button
                    }
                    if (!email.endsWith("@wvsu.edu.ph")) {
                        errorMessage = "Please use your WVSU email (@wvsu.edu.ph)"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }
                    if (password.length < 6) {
                        errorMessage = "Password should be at least 6 characters"
                        return@Button
                    }
                    
                    // Proceed to step two without creating the account yet
                    onJoinClick(email, password)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001229))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = WestconYellow, modifier = Modifier.size(24.dp))
                } else {
                    Text("Join WESTCON", color = WestconYellow, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = MomotrustFontFamily)
                }
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
