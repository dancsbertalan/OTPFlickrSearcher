package com.bertalandancs.otpflickrsearcher.data.repositories

import com.bertalandancs.otpflickrsearcher.data.api.FlickrService
import com.bertalandancs.otpflickrsearcher.data.model.GetInfoPhoto
import com.bertalandancs.otpflickrsearcher.data.model.Photos
import com.bertalandancs.otpflickrsearcher.data.model.RspStatus
import io.reactivex.rxjava3.core.Observable

interface ImagesRepository {
    fun getImages(
        perPage: Int = 20,
        page: Int = 1,
        text: String,
        extras: String? = null
    ): Observable<SearchResponse>

    fun getImageById(
        photoId: Long?
    ): Observable<InfoResponse>
}

class ImagesRepositoryImpl(private val service: FlickrService) : ImagesRepository {
    override fun getImages(
        perPage: Int,
        page: Int,
        text: String,
        extras: String?
    ): Observable<SearchResponse> =
        service.getImages(perPage, page, text, extras).flatMap {
            if (RspStatus.valueOf(it.stat) == RspStatus.ok)
                Observable.just(SearchResponse.StatusOk(it.photos))
            else
                Observable.just(SearchResponse.StatusFailed(it.error.code))
        }

    override fun getImageById(photoId: Long?): Observable<InfoResponse> =
        service.getImageById(photoId).flatMap {
            if (RspStatus.valueOf(it.stat) == RspStatus.ok)
                Observable.just(InfoResponse.StatusOk(it.photo))
            else
                Observable.just(InfoResponse.StatusFailed(it.error.code))
        }
}

open class InfoResponse {
    class StatusFailed(val errorCode: Int) : InfoResponse()
    class StatusOk(val photoInfoPhoto: GetInfoPhoto) : InfoResponse()
}

open class SearchResponse {
    class StatusFailed(val errorCode: Int) : SearchResponse()
    class StatusOk(val photos: Photos) : SearchResponse()
}