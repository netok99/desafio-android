package com.picpay.desafio.android.contact.di

import com.picpay.desafio.android.contact.viewmodel.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val contactModule = module {
    viewModel { ContactViewModel(get()) }
}
