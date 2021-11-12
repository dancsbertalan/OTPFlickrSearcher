package com.bertalandancs.otpflickrsearcher.data.repositories

import com.bertalandancs.otpflickrsearcher.data.api.FlickrService
import com.bertalandancs.otpflickrsearcher.data.model.Rsp
import io.reactivex.rxjava3.core.Observable

interface ImagesRepository {
    fun getImages(): Observable<Rsp>
}

class ImagesRepositoryImpl(private val service: FlickrService) : ImagesRepository {
    override fun getImages(): Observable<Rsp> {
        TODO("Implement fetch datas from service if we are online otherwise show a message for user")
    }
}