package com.picpay.desafio.android.data.di

import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.BuildConfig
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT = 15L
const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

val dataModule = module {
    single { GsonBuilder().apply { setLenient() }.create() }

    single<Interceptor> {
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(get<Interceptor>())
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

    factory<UserRepository> { UserRepositoryImpl(get()) }
}
