package com.picpay.desafio.android.contact.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.contact.domain.GetUsersState
import com.picpay.desafio.android.contact.domain.GetUsersUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class ContactViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    val action = MutableLiveData<GetUsersState>()

    fun fetchUsers() = viewModelScope.launch {
        getUsersUseCase().collect { action.postValue(it) }
    }
}
