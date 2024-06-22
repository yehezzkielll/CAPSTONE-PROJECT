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
import com.bangkit.deteksitanaman.data.models.auth.LoginRequest
import com.bangkit.deteksitanaman.data.models.response.LoginResponse
import com.bangkit.deteksitanaman.data.models.response.LoginResult
import com.bangkit.deteksitanaman.data.repository.ResultApi
import com.bangkit.deteksitanaman.databinding.ActivityLoginBinding
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.view.home.MainActivity
import com.bangkit.deteksitanaman.view.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.loginBtn.setOnClickListener { login() }
        binding.toRegisterText.setOnClickListener { moveToRegister() }
    }

    private fun login() {
        val username = binding.emailEt.text.toString()
        val password = binding.passEt.text.toString()
        when {
            username.isEmpty() -> {
                binding.emailEt.error = getString(R.string.empty_field)
            }
            password.isEmpty() -> {
                binding.passEt.error = "Silahkan tulis password Anda"
            }
            else -> {
                val loginRequest = LoginRequest(username, password)
                viewModel.loginUser(loginRequest).observe(this) { response ->
                    when(response) {
                        is ResultApi.Loading -> {
                            showLoading(true)
                        }
                        is ResultApi.Success -> {
                            showLoading(false)
                            loginSuccessProcess(response.data)
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

    private fun loginSuccessProcess(data: LoginResponse) {
        val loginResult = LoginResult(
            idUser = data.user.idUser,
            nameUser = data.user.nameUser,
            phonenumber = data.user.phonenumber,
            token = data.token,
            isLogin = true
        )
        viewModel.saveSession(loginResult)
        Log.d("LoginActivity", "loginSuccessProcess: ${loginResult.token}")
        showToast("success")
        moveToMain()
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

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun moveToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}