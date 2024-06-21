package com.bangkit.deteksitanaman.data.models.auth

data class RegisterRequest(
    val name_user: String,
    val username: String,
    val password: String,
    val phonenumber: String
)