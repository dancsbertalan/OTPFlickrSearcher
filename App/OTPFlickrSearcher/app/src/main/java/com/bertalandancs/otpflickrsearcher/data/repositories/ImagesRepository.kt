package com.bertalandancs.otpflickrsearcher.data.repositories

import com.bertalandancs.otpflickrsearcher.data.api.FlickrService
import com.bertalandancs.otpflickrsearcher.data.model.Photo
import com.bertalandancs.otpflickrsearcher.data.model.RspStatus
import io.reactivex.rxjava3.core.Observable

interface ImagesRepository {
    fun getImages(
        perPage: Int = 20,
        page: Int = 1,
        text: String,
        extras: String? = null
    ): Observable<GetImagesResponse>
}

class ImagesRepositoryImpl(private val service: FlickrService) : ImagesRepository {
    override fun getImages(
        perPage: Int,
        page: Int,
        text: String,
        extras: String?
    ): Observable<GetImagesResponse> =
        service.getImages(perPage, page, text, extras).flatMap {
            if (RspStatus.valueOf(it.stat) == RspStatus.ok)
                Observable.just(GetImagesResponse.StatusOk(it.photos.photoList))
            else
                Observable.just(GetImagesResponse.StatusFailed(it.error.code))

        }
}

open class GetImagesResponse {
    class StatusFailed(val errorCode: Int) : GetImagesResponse()
    class StatusOk(val photos: List<Photo>?) : GetImagesResponse()
}