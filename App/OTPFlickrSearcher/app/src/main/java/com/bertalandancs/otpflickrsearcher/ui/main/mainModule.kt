package com.bertalandancs.otpflickrsearcher.ui.main

import android.app.Application
import android.content.Context
import com.bertalandancs.otpflickrsearcher.App
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val APP_SHARED_PREFS = "APP_SHARED_PREFS"

val mainModule = module {
    fragment{
        MainFragment(get(),get())
    }

    viewModel {
        MainViewModel(get(), get())
    }
}

val sharedPreferencesModule = module {

    fun provideSharedPreferences(app: Application) =
        app.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)

    single {
        provideSharedPreferences(get())
    }
}