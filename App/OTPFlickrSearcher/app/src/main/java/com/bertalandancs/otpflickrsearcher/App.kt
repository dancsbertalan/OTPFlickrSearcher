package com.bertalandancs.otpflickrsearcher

import android.app.Application
import com.bertalandancs.otpflickrsearcher.data.api.flickrServiceModule
import com.bertalandancs.otpflickrsearcher.data.networkModule
import com.bertalandancs.otpflickrsearcher.data.repositories.imagesRepositoryModule
import com.bertalandancs.otpflickrsearcher.ui.details.detailsModule
import com.bertalandancs.otpflickrsearcher.ui.main.mainModule
import com.bertalandancs.otpflickrsearcher.ui.main.sharedPreferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import timber.log.Timber

import timber.log.Timber.*


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        //TODO: Otherwise logging to crash reporting lib.

        startKoin {
            androidLogger()
            androidContext(this@App)
            fragmentFactory()
            modules(
                flickrServiceModule,
                imagesRepositoryModule,
                networkModule,
                mainModule,
                detailsModule,
                sharedPreferencesModule
            )
        }
    }

}