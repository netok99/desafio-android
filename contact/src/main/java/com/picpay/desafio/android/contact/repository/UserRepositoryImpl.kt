package com.picpay.desafio.android.contact.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.contact.domain.UserRepository
import com.picpay.desafio.android.contact.model.User
import com.picpay.desafio.android.contact.remote.PicPayService

const val EMPTY_LIST_MESSAGE = "Empty List"

class UserRepositoryImpl(private val picPayService: PicPayService) : UserRepository {
    override suspend fun getUsers(): Either<String, List<User>> =
        with(picPayService.getUsers()) {
            if (isEmpty()) EMPTY_LIST_MESSAGE.left() else right()
        }
}
