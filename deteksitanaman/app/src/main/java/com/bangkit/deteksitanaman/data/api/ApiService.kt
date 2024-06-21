package com.bangkit.deteksitanaman.data.api

import com.bangkit.deteksitanaman.data.models.auth.LoginRequest
import com.bangkit.deteksitanaman.data.models.auth.RegisterRequest
import com.bangkit.deteksitanaman.data.models.response.LoginResponse
import com.bangkit.deteksitanaman.data.models.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("signup")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}