package com.picpay.desafio.android.contact.di

import com.picpay.desafio.android.contact.domain.GetUsersUseCase
import com.picpay.desafio.android.contact.domain.UserRepository
import com.picpay.desafio.android.contact.remote.PicPayService
import com.picpay.desafio.android.contact.repository.UserRepositoryImpl
import com.picpay.desafio.android.contact.view.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

const val TIMEOUT = 15L
const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

val contactModule = module {
    single { get<Retrofit>().create(PicPayService::class.java) }

    viewModel { ContactViewModel(get()) }

    single { GetUsersUseCase(get()) }

    factory<UserRepository> { UserRepositoryImpl(get()) }
}
