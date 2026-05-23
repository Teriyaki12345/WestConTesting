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
                clearBackStackAndNavigate(LandingFragment())
            },
            onMessageClick = { chatId, userName, otherUserUid ->
                navigateTo(ChatDetailFragment.newInstance(chatId, otherUserUid, userName))
            },
            onProfileClick = { userId ->
                navigateTo(ProfileFragment.newInstance(userId))
            }
        )
    }
}
