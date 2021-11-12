package com.bertalandancs.otpflickrsearcher.data.api

import org.koin.dsl.module
import retrofit2.Retrofit


val flickrServiceModule = module {

    fun provideFlickrService(retrofit: Retrofit): FlickrService {
        return retrofit.create(FlickrService::class.java)
    }

    single { provideFlickrService(get()) }

}