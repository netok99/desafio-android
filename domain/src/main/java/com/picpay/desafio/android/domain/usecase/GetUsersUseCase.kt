package com.picpay.desafio.android.domain.usecase

import arrow.core.Either
import com.picpay.desafio.android.domain.entity.UserEntity
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

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
data class GetUsersSuccess(val users: List<UserEntity>) : GetUsersState()
object GetUsersLoading : GetUsersState()
object GetUsersError : GetUsersState()
