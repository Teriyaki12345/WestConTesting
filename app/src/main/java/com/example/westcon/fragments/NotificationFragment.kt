package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.NotificationScreen

class NotificationFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        NotificationScreen(
            onBackClick = { parentFragmentManager.popBackStack() }
        )
    }
}
