package com.picpay.desafio.android.di

import com.google.gson.GsonBuilder
import com.picpay.desafio.android.contact.BuildConfig
import com.picpay.desafio.android.contact.di.BASE_URL
import com.picpay.desafio.android.contact.di.TIMEOUT
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
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
}
