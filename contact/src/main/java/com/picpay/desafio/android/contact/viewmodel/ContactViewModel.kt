package com.picpay.desafio.android.contact.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.usecase.GetUsersState
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    init {
        fetchUsers()
    }

    val action = MutableLiveData<GetUsersState>()

    fun fetchUsers() = viewModelScope.launch {
        getUsersUseCase().collect { action.postValue(it) }
    }
}
