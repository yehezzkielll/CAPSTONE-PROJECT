package com.bangkit.deteksitanaman.data.models.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: String
)

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name_user")
	val nameUser: String,

	@field:SerializedName("id_account")
	val idAccount: String,

	@field:SerializedName("phonenumber")
	val phonenumber: String,

	@field:SerializedName("created_at")
	val createdAt: CreatedAt,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("username")
	val username: String
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class LoginResult (

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("name_user")
	val nameUser: String,

	@field:SerializedName("phonenumber")
	val phonenumber: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("isLogin")
	val isLogin: Boolean = false

)
