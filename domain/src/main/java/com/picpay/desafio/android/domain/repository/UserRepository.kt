package com.picpay.desafio.android.domain.repository

import arrow.core.Either
import com.picpay.desafio.android.domain.entity.UserEntity

interface UserRepository {
    suspend fun getUsers(): Either<String, List<UserEntity>>
}
