package com.mercadobitcoin.membergetmember.di

import com.mercadobitcoin.membergetmember.view.MgmViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mgmModule = module {
    viewModel { MgmViewModel(get()) }
}
