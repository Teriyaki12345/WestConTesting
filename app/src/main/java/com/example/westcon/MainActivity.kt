package com.example.westcon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.westcon.ui.theme.WestConTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Makes the UI draw behind the status bar for that full-screen background effect
        enableEdgeToEdge()

        setContent {
            WestConTheme {
                // Tracking 4 screens now: landing, signup, signup2, and join
                var currentScreen by remember { mutableStateOf("landing") }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        "landing" -> {
                            WestconLoginScreen(
                                onSignUpClick = { currentScreen = "signup" },
                                onLoginClick = { currentScreen = "join" }
                            )
                        }

                        "signup" -> {
                            // RegisterScreen is from signup.kt
                            RegisterScreen(
                                // When user clicks "Join WESTCON", go to the next step
                                onJoinClick = { currentScreen = "signup2" },
                                onBackClick = { currentScreen = "landing" }
                            )
                        }

                        "signup2" -> {
                            // SignUpStepTwoScreen is from signup2.kt
                            SignUpStepTwoScreen(
                                onNextClick = {
                                    // Eventually move to Home Screen here
                                    currentScreen = "landing"
                                }
                            )
                        }

                        "join" -> {
                            // LoginScreen is from join.kt
                            LoginScreen(
                                onBackClick = { currentScreen = "landing" }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WestConTheme {
        Greeting("Android")
    }
}