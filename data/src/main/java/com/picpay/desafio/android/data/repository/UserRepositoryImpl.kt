package com.picpay.desafio.android.data.repository

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.traverseEither
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.domain.entity.*
import com.picpay.desafio.android.domain.repository.UserRepository

class UserRepositoryImpl(private val picPayService: PicPayService) : UserRepository {
    override suspend fun getUsers(): Either<String, List<UserEntity>> =
        with(picPayService.getUsers()) {
            if (isEmpty()) return "Empty List".left()
            return this.traverseEither { user ->
                user.createUserEntity()
            }
        }

    private suspend fun User.createUserEntity(): Either<String, UserEntity> {
        val user = this
        val item: Either<String, UserEntity> = either {
            UserEntity(
                image = Image.create(user.image).bind(),
                name = Name.create(user.name).bind(),
                id = Id(user.id),
                username = Username(user.username)
            )
        }
        return item
    }
}
