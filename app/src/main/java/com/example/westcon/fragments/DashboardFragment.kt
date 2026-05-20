package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.DashboardScreen

class DashboardFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        DashboardScreen(
            onNotificationClick = { navigateTo(NotificationFragment()) },
            onSearchClick = { navigateTo(SearchFragment()) },
            onLogoutClick = {
                com.example.westcon.data.FirebaseManager.logout()
                navigateTo(LandingFragment())
            },
            onMessageClick = { chatId, userName ->
                navigateTo(ChatDetailFragment.newInstance(chatId, userName))
            }
        )
    }
}
