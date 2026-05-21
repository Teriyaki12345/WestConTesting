package com.example.westcon.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.westcon.R
import com.example.westcon.ui.theme.WestConTheme

abstract class BaseFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Fragment Created")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: UI View being created")
        return ComposeView(requireContext()).apply {
            setContent {
                WestConTheme {
                    ScreenContent()
                }
            }
        }
    }

    @Composable
    abstract fun ScreenContent()

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Fragment visible to user")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Fragment interactive")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Fragment losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Fragment no longer visible")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Clean up UI resources")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Final clean up")
    }

    // Helper to navigate between fragments
    fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun clearBackStackAndNavigate(fragment: Fragment) {
        // Pop everything from the back stack
        parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        
        // Navigate to the new fragment without adding to back stack
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
