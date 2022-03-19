package com.picpay.desafio.android.contact.remote

import com.picpay.desafio.android.contact.model.User
import retrofit2.http.GET

interface PicPayService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
