package com.bangkit.deteksitanaman.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.deteksitanaman.data.api.ApiService
import com.bangkit.deteksitanaman.data.local.DetectEntity
import com.bangkit.deteksitanaman.data.local.DetectHistoryDao
import com.bangkit.deteksitanaman.data.models.auth.LoginRequest
import com.bangkit.deteksitanaman.data.models.auth.RegisterRequest
import com.bangkit.deteksitanaman.data.models.response.LoginResponse
import com.bangkit.deteksitanaman.data.models.response.LoginResult
import com.bangkit.deteksitanaman.data.models.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DataRepository(
    private val apiService: ApiService,
    private val pref: UserPreference,
    private val context: Context,
    private val detectHistoryDao: DetectHistoryDao
) {

    suspend fun saveSession(user: LoginResult) {
        pref.saveSession(user)
    }

    fun getUser(): Flow<LoginResult> {
        return pref.getUser()
    }

    suspend fun logout() {
        pref.logout()
    }

    suspend fun registerUser(registerRequest: RegisterRequest): ResultApi<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.registerUser(registerRequest)
                ResultApi.Success(response)
            } catch (exception: Exception) {
                ResultApi.Error(exception.toString())
            }
        }
    }

    suspend fun loginUser(loginRequest: LoginRequest): ResultApi<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.loginUser(loginRequest)
                ResultApi.Success(response)
            } catch (exception: Exception) {
                ResultApi.Error(exception.toString())
            }
        }
    }

    // database

    suspend fun addScanToHistory(scanHistory: DetectEntity) {
        detectHistoryDao.addScanToHistory(scanHistory)
    }

    suspend fun getScanHistory(): List<DetectEntity> {
        return detectHistoryDao.getScanHistory()
    }

    suspend fun deleteScanHistory(id: Int) {
        detectHistoryDao.clearScanHistory(id)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            context: Context,
            detectHistoryDao: DetectHistoryDao
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, userPreference, context, detectHistoryDao)
            }.also { instance = it }
    }
}

