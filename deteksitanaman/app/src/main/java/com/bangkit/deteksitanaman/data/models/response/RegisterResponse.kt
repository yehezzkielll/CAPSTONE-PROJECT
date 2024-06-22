package com.bangkit.deteksitanaman.data.models.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name_user")
	val nameUser: String,

	@field:SerializedName("id_account")
	val idAccount: String,

	@field:SerializedName("phonenumber")
	val phonenumber: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("username")
	val username: String
)
