package com.bangkit.deteksitanaman.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.data.repository.UserPreference
import com.bangkit.deteksitanaman.data.repository.dataStore
import com.bangkit.deteksitanaman.view.auth.LoginActivity
import com.bangkit.deteksitanaman.view.home.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference.getInstance(applicationContext.dataStore)
        MainScope().launch {
            userPreference.getUser().asLiveData().observe(this@WelcomeActivity) { user ->
                if (user.isLogin) {
                    navigateToMain()
                } else {
                    navigateToLogin()
                }
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}