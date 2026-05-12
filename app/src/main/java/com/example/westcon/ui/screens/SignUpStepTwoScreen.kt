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
import com.example.westcon.data.FirebaseManager
import com.example.westcon.data.UserProfile

@Composable
fun SignUpStepTwoScreen(onNextClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("CICT") }
    var program by remember { mutableStateOf("BSCS") }
    var year by remember { mutableStateOf("1") }
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
                .padding(top = 130.dp), // Adjusted top padding slightly for better fit
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Before we begin,\ntell us about\nyourself!",
                color = Color.White,
                fontSize = 42.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MomotrustFontFamily
            )

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))

            SignUpTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                icon = R.drawable.person // Changed to person icon to match "Username"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DropdownLabel("College")
                    CustomDropdown(listOf("CICT", "CON", "COE", "CAS"), college) { college = it }
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    DropdownLabel("Program")
                    CustomDropdown(listOf("BSIT", "BSCS", "BSIS"), program) { program = it }
                }
                Column(modifier = Modifier.weight(0.8f)) {
                    DropdownLabel("Year Lvl.")
                    CustomDropdown(listOf("1", "2", "3", "4"), year) { year = it }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (username.isEmpty()) {
                        errorMessage = "Please enter a username"
                        return@Button
                    }
                    val user = FirebaseManager.getCurrentUser()
                    if (user == null) {
                        errorMessage = "User session not found"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        val profile = UserProfile(
                            uid = user.uid,
                            name = username,
                            email = user.email ?: "",
                            department = college,
                            course = program,
                            year = year
                        )
                        val result = FirebaseManager.saveUserProfile(profile)
                        isLoading = false
                        if (result.isSuccess) {
                            onNextClick()
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Failed to save profile"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001229))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = WestconYellow, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Next",
                        color = WestconYellow,
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
fun DropdownLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = MomotrustFontFamily,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(15.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MomotrustFontFamily
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}