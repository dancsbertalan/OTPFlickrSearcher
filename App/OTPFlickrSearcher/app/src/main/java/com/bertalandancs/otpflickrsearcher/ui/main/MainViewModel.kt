package com.bertalandancs.otpflickrsearcher.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.bertalandancs.otpflickrsearcher.data.model.Photos
import com.bertalandancs.otpflickrsearcher.data.repositories.SearchResponse
import com.bertalandancs.otpflickrsearcher.data.repositories.ImagesRepository
import com.bertalandancs.otpflickrsearcher.ui.main.model.ThumbnailImage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//SearchStatus and InfoStatus is similar maybe i can clean up (?)
open class SearchStatus {
    data class Ok(val thumbnails: List<ThumbnailImage>, val page: Int) : SearchStatus()

    data class Fail(val errorCode: Int) : SearchStatus()
    object InternetError : SearchStatus()
    data class UnknownError(val throwable: Throwable) : SearchStatus()
    data class Loading(val isLoading: Boolean) : SearchStatus()
}

class MainViewModel(
    private val imagesRepository: ImagesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var currentQuery = ""
    private lateinit var currentPhotos: Photos
    private val search = PublishSubject.create<SearchStatus>()
    val searchStatus: Observable<SearchStatus> = search
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getImages(queryString: String, page: Int = 1) {
        disposable.add(imagesRepository.getImages(
            page = page,
            text = queryString,
            extras = "url_n,url_t"
        ).doOnSubscribe {
            sharedPreferences.edit().putString(LAST_SEARCH, queryString).apply()
            currentQuery = queryString
            search.onNext(SearchStatus.Loading(true))
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                search.onNext(SearchStatus.Loading(false))

                if (it is SearchResponse.StatusOk) {
                    currentPhotos = it.photos
                    if (it.photos.photoList != null) {
                        val thumbnails = ArrayList<ThumbnailImage>()
                        it.photos.photoList.forEach { photo ->
                            if (!photo.urlN.isNullOrEmpty())
                                thumbnails.add(ThumbnailImage(photo.id, photo.urlN))
                            else
                                thumbnails.add(ThumbnailImage(photo.id, photo.urlT))
                        }
                        search.onNext(SearchStatus.Ok(thumbnails, currentPhotos.page))
                    }
                } else if (it is SearchResponse.StatusFailed)
                    search.onNext(SearchStatus.Fail(it.errorCode))
            }, {
                Timber.e("getImages error: $it")
                search.onNext(SearchStatus.Loading(false))
                when (it) {
                    is SocketTimeoutException,
                    is UnknownHostException -> search.onNext(SearchStatus.InternetError)
                    else -> search.onNext(SearchStatus.UnknownError(it))
                }
            })
        )
    }

    fun nextImages() {
        val nextPage = currentPhotos.page + 1
        if (nextPage <= currentPhotos.pages)
            getImages(currentQuery, nextPage)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    companion object {
        private const val LAST_SEARCH: String = "LAST_SEARCH"
    }
}