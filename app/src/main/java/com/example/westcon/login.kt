package com.example.westcon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
// CRITICAL: You were missing these for 'remember' and 'mutableStateOf'
import androidx.compose.runtime.* import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Note: Ensure these are accessible to your signup.kt file!
val MomotrustFontFamily = FontFamily(
    Font(R.font.momotrustsans_regular, FontWeight.Normal),
    Font(R.font.momotrustdisplay_regular, FontWeight.Bold)
)

val WestconDarkBlue = Color(0xFF001D3D)
val WestconYellow = Color(0xFFFFD700)

@Composable
fun WestconLoginScreen(onSignUpClick: () -> Unit, onLoginClick: () -> Unit) {
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
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    tint = WestconYellow,
                    modifier = Modifier.size(110.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("WESTCON", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = MomotrustFontFamily)
                    Text("THE OFFICIAL STUDENT SKILL\nMARKETPLACE", color = Color.White, fontSize = 10.sp, lineHeight = 12.sp, fontFamily = MomotrustFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Let’s get you\nsorted", color = Color.White, fontSize = 36.sp, lineHeight = 46.sp, fontWeight = FontWeight.Bold, fontFamily = MomotrustFontFamily)
            Spacer(modifier = Modifier.height(24.dp))
            Text("WANT TO CONNECT WITH FELLOW\nTAGA-WESTS?", color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp, fontFamily = MomotrustFontFamily)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WestconDarkBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.tdesign_education_filled), null, tint = WestconYellow, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Sign up with Student ID", color = WestconYellow, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = MomotrustFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLoginClick, // 3. Set this to the parameter
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.person),
                        contentDescription = null,
                        tint = WestconDarkBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Login with Student ID",
                        color = WestconDarkBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MomotrustFontFamily
                    )
                }
            }
        }

        FooterSection(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun SignUpTextField(
    label: String,
    icon: Int,
    isPassword: Boolean = false,
    showEyeIcon: Boolean = false
) {
    var textValue by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textValue,
        onValueChange = { textValue = it },
        placeholder = { Text(label, color = Color.Gray) },
        leadingIcon = {
            Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(20.dp), tint = WestconDarkBlue)
        },
        trailingIcon = {
            if (isPassword && showEyeIcon) {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.secret)
                else
                    painterResource(id = R.drawable.secret_on)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null, modifier = Modifier.size(24.dp), tint = WestconDarkBlue)
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = WestconYellow,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = true
    )
}

@Composable
fun FooterSection(modifier: Modifier) {
    Column(
        modifier = modifier.padding(bottom = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ACADEMIC INTEGRITY • PEER COLLABORATION • WVSU EXCELLENCE", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, fontFamily = MomotrustFontFamily)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text("Privacy Policy", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontFamily = MomotrustFontFamily)
            Text("  •  ", color = Color.White.copy(alpha = 0.8f))
            Text("Terms of Service", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, fontFamily = MomotrustFontFamily)
        }
    }
}