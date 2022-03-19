package com.picpay.desafio.android.di

import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule = module {
    single { GsonBuilder().apply { setLenient() }.create() }
}
