package com.bertalandancs.otpflickrsearcher.data.api

import com.bertalandancs.otpflickrsearcher.data.model.GetInfoRsp
import com.bertalandancs.otpflickrsearcher.data.model.SearchRsp
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("?method=flickr.photos.search")
    fun getImages(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("text") text: String,
        @Query("extras") extras: String? = null
    ): Observable<SearchRsp>

    @GET("?method=flickr.photos.getInfo")
    fun getImageById(
        @Query("photo_id") photoId: Long?
    ): Observable<GetInfoRsp>
}

