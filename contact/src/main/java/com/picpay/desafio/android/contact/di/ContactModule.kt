package com.picpay.desafio.android.contact.di

import com.picpay.desafio.android.contact.BuildConfig
import com.picpay.desafio.android.contact.domain.GetUsersUseCase
import com.picpay.desafio.android.contact.remote.PicPayService
import com.picpay.desafio.android.contact.repository.UserRepository
import com.picpay.desafio.android.contact.repository.UserRepositoryImpl
import com.picpay.desafio.android.contact.view.ContactViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT = 15L
const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

val contactModule = module {
    single<Interceptor>(named("httpLoggingInterceptor")) {
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
    }

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    single {
        Retrofit
            .Builder()
            .client(get())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>().create(PicPayService::class.java) }

    viewModel { ContactViewModel(get()) }

    single { GetUsersUseCase(get()) }

    factory<UserRepository> { UserRepositoryImpl(get()) }
}
