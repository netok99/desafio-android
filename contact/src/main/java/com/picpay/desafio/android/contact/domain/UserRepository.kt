package com.picpay.desafio.android.contact.domain

import arrow.core.Either
import com.picpay.desafio.android.contact.model.User

interface UserRepository {
    suspend fun getUsers(): Either<String, List<User>>
}
