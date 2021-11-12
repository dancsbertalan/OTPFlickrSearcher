package com.bertalandancs.otpflickrsearcher

import android.app.Application
import com.bertalandancs.otpflickrsearcher.data.api.flickrServiceModule
import com.bertalandancs.otpflickrsearcher.data.networkModule
import com.bertalandancs.otpflickrsearcher.data.repositories.imagesRepositoryModule
import com.bertalandancs.otpflickrsearcher.ui.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                flickrServiceModule,
                imagesRepositoryModule,
                networkModule,
                mainModule
            )
        }
    }

}