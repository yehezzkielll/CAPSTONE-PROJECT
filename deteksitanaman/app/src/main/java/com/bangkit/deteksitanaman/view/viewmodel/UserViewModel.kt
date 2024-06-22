package com.bangkit.deteksitanaman.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bangkit.deteksitanaman.data.models.auth.LoginRequest
import com.bangkit.deteksitanaman.data.models.auth.RegisterRequest
import com.bangkit.deteksitanaman.data.models.response.LoginResult
import com.bangkit.deteksitanaman.data.repository.DataRepository
import com.bangkit.deteksitanaman.data.repository.ResultApi
import kotlinx.coroutines.launch

class UserViewModel(private val repository: DataRepository) : ViewModel() {

    fun saveSession(user: LoginResult) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun registerUser(registerRequest: RegisterRequest) = liveData {
        emit(ResultApi.Loading)
        val result = repository.registerUser(registerRequest)
        emit(result)
    }

    fun loginUser(loginRequest: LoginRequest) = liveData {
        emit(ResultApi.Loading)
        val result = repository.loginUser(loginRequest)
        emit(result)
    }
}