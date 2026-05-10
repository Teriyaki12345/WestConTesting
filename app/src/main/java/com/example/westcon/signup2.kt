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

@Composable
fun SignUpStepTwoScreen(onNextClick: () -> Unit) {
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

            Spacer(modifier = Modifier.height(30.dp))

            SignUpTextField(
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
                    CustomDropdown(listOf("CICT", "CON", "COE", "CAS"))
                }
                Column(modifier = Modifier.weight(1.2f)) {
                    DropdownLabel("Program")
                    CustomDropdown(listOf("BSIT", "BSCS", "BSIS"))
                }
                Column(modifier = Modifier.weight(0.8f)) {
                    DropdownLabel("Year Lvl.")
                    CustomDropdown(listOf("1", "2", "3", "4"))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001229))
            ) {
                Text(
                    "Next",
                    color = WestconYellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MomotrustFontFamily
                )
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
fun CustomDropdown(options: List<String>) { // Fixed: was 'listOf<String>' (syntax error)
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            // Fixed: use Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true) or simply menuAnchor()
            // depending on your Material3 version
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
                        selectedOption = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}