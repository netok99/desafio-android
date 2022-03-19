package com.picpay.desafio.android.contact.domain

import arrow.core.Either
import com.picpay.desafio.android.contact.model.User
import com.picpay.desafio.android.contact.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GetUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<GetUsersState> = flow {
        val response = repository.getUsers()
        emit(
            if (response is Either.Right) GetUsersSuccess(response.value)
            else GetUsersError
        )
    }.onStart {
        emit(GetUsersLoading)
    }.catch {
        emit(GetUsersError)
    }.flowOn(Dispatchers.IO)
}

sealed class GetUsersState
data class GetUsersSuccess(val users: List<User>) : GetUsersState()
object GetUsersLoading : GetUsersState()
object GetUsersError : GetUsersState()
