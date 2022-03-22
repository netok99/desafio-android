package com.picpay.desafio.android.contact.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.entity.UserEntity
import com.picpay.desafio.android.domain.usecase.GetUsersState
import com.picpay.desafio.android.domain.usecase.GetUsersSuccess
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.launch

const val STATE_KEY = "state_key"

class ContactViewModel(
    private val state: SavedStateHandle,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    init {
        fetchUsers()
    }

    val action = MutableLiveData<GetUsersState>()

    fun fetchUsers() = viewModelScope.launch {
        getUsersUseCase().collect {
            action.postValue(it)
            saveState(it)
        }
    }

    //region exercise
    private fun saveState(it: GetUsersState) {
        if (it is GetUsersSuccess) state.set(STATE_KEY, it.users)
    }

    fun getState() = state.getLiveData<List<UserEntity>>(STATE_KEY)
    //endregion
}
