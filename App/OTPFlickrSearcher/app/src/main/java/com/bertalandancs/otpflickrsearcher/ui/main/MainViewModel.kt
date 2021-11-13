package com.bertalandancs.otpflickrsearcher.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bertalandancs.otpflickrsearcher.data.repositories.GetImagesResponse
import com.bertalandancs.otpflickrsearcher.data.repositories.ImagesRepository
import com.bertalandancs.otpflickrsearcher.ui.main.model.ThumbnailImage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class SearchStatus {
    data class Ok(val thumbnails: List<ThumbnailImage>) : SearchStatus()
    data class Fail(val errorCode: Int) : SearchStatus()

    object InternetError : SearchStatus()
    data class UnknownError(val throwable: Throwable) : SearchStatus()
    data class Loading(val isLoading: Boolean) : SearchStatus()
}

class MainViewModel(private val imagesRepository: ImagesRepository) : ViewModel() {
    private val TAG: String = "MainViewModel"
    private val disposable: CompositeDisposable = CompositeDisposable()

    private val search = PublishSubject.create<SearchStatus>()
    val images: Observable<SearchStatus> = search
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getImages(queryString: String, page: Int = 1) =
        disposable.add(imagesRepository.getImages(
            page = page,
            text = queryString,
            extras = "url_n"
        ).doOnSubscribe {
            search.onNext(SearchStatus.Loading(true))
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                search.onNext(SearchStatus.Loading(false))

                if (it is GetImagesResponse.StatusOk) {
                    if (it.photos != null) {
                        val thumbnails = ArrayList<ThumbnailImage>()
                        it.photos.forEach { photo ->
                            thumbnails.add(ThumbnailImage(photo.id, photo.urlN))
                        }
                        search.onNext(SearchStatus.Ok(thumbnails))
                    }
                } else if (it is GetImagesResponse.StatusFailed)
                    search.onNext(SearchStatus.Fail(it.errorCode))
            }, {
                Log.e(TAG, "getImages error: $it")
                search.onNext(SearchStatus.Loading(false))
                when (it) {
                    is SocketTimeoutException,
                    is UnknownHostException -> search.onNext(SearchStatus.InternetError)
                    else -> search.onNext(SearchStatus.UnknownError(it))
                }
            })
        )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}