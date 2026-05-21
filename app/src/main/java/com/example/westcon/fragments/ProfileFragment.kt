package com.example.westcon.fragments

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.westcon.ui.screens.ProfileScreen
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment() {
    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    @Composable
    override fun ScreenContent() {
        val userId = arguments?.getString(ARG_USER_ID)
        ProfileScreen(
            userId = userId,
            onLogoutClick = {
                com.example.westcon.data.FirebaseManager.logout()
                clearBackStackAndNavigate(LandingFragment())
            },
            onBackClick = {
                parentFragmentManager.popBackStack()
            },
            onMessageClick = { authorUid, authorName ->
                val currentUid = com.example.westcon.data.FirebaseManager.getCurrentUser()?.uid ?: ""
                if (currentUid != authorUid) {
                    val chatId = if (currentUid < authorUid) "${currentUid}_${authorUid}" else "${authorUid}_$currentUid"
                    navigateTo(ChatDetailFragment.newInstance(chatId, authorName))
                }
            },
            onExchangeClick = { authorUid ->
                // For now, show a toast or handle exchange request
                android.widget.Toast.makeText(context, "Exchange request sent!", android.widget.Toast.LENGTH_SHORT).show()
                
                // Optional: Send a notification via FirebaseManager if you have a general method
                lifecycleScope.launch {
                    val currentUid = com.example.westcon.data.FirebaseManager.getCurrentUser()?.uid ?: ""
                    val currentUserProfile = com.example.westcon.data.FirebaseManager.getUserProfile(currentUid)
                    
                    val notification = com.example.westcon.data.Notification(
                        receiverUid = authorUid,
                        senderUid = currentUid,
                        senderName = currentUserProfile?.name ?: "Someone",
                        senderIconName = currentUserProfile?.profileIconName ?: "Person",
                        type = "SKILL_EXCHANGE",
                        title = "New Exchange Request",
                        content = "${currentUserProfile?.name ?: "Someone"} wants to exchange skills with you!"
                    )
                    com.example.westcon.data.FirebaseManager.sendNotification(notification)
                }
            }
        )
    }
}
