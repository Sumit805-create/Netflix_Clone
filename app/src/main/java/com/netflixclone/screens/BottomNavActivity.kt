package com.netflixclone.screens

import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.netflixclone.R
import com.netflixclone.databinding.ActivityBottomNavBinding
import com.netflixclone.helpers.AuthTokenManager
import com.netflixclone.network.models.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomNavActivity : BaseActivity() {
    private lateinit var binding: ActivityBottomNavBinding
    private lateinit var authTokenManager: AuthTokenManager
    // Flags to know whether bottom tab fragments are displayed at least once
    private val fragmentFirstDisplay = mutableListOf(false, false, false)

    private val feedFragment = FeedFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = feedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupUI()
    }



    // Function to validate the token
    private fun validateToken(token: String) {

    }

    // Redirect to Login screen
    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun setupUI() {
        fragmentManager.beginTransaction().apply {
            add(R.id.container, feedFragment, "home")
        }.commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is FeedFragment) {
            // Handle any fragment initialization if needed
        }
    }

    fun onFeedFragmentViewCreated() {
        if (!fragmentFirstDisplay[0]) {
            fragmentFirstDisplay[0] = true
            feedFragment.onFirstDisplay()
        }
    }
}
