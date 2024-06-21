package com.bangkit.deteksitanaman.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.data.models.auth.RegisterRequest
import com.bangkit.deteksitanaman.data.repository.ResultApi
import com.bangkit.deteksitanaman.databinding.ActivityRegisterBinding
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.view.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        binding.registerBtn.setOnClickListener { register() }
        binding.toLoginText.setOnClickListener { navigateToLogin() }
    }

    private fun register() {
        val fullName = binding.fullNameEt.text.toString()
        val username = binding.usernameEt.text.toString()
        val phoneNumber = binding.phoneNumberEt.text.toString()
        val password = binding.passEt.text.toString()

        when {
            fullName.isEmpty() -> {
                binding.fullNameEt.error = getString(R.string.empty_field)
            }
            username.isEmpty() -> {
                binding.usernameEt.error = getString(R.string.empty_field)
            }
            phoneNumber.isEmpty() -> {
                binding.phoneNumberEt.error = getString(R.string.empty_field)
            }
            password.isEmpty() -> {
                binding.passEt.error = getString(R.string.empty_field)
            }
            else -> {
                val registerRequest = RegisterRequest(fullName, username, phoneNumber, password)
                viewModel.registerUser(registerRequest).observe(this@RegisterActivity) { response ->
                    when(response) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }
                        is ResultApi.Success -> {
                            showLoading(false)
                            navigateToLogin()
                        }
                        is ResultApi.Error -> {
                            showLoading(false)
                            showToast(response.error)
                            Log.d("RegisterActivity", "register: ${response.error}")
                        }
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(onLoading: Boolean) {
        if (onLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}