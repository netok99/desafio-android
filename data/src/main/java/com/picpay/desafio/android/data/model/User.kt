package com.picpay.desafio.android.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("img") val image: String?,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String
)
