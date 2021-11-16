package com.bertalandancs.otpflickrsearcher.data.repositories

import com.bertalandancs.otpflickrsearcher.data.api.FlickrService
import org.koin.dsl.module

val imagesRepositoryModule = module {
    fun provideImagesRepository(flickrService: FlickrService): ImagesRepository {
        return ImagesRepositoryImpl(flickrService)
    }

    single {
        provideImagesRepository(get())
    }

}