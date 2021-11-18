package com.bertalandancs.otpflickrsearcher.ui.details

import androidx.lifecycle.ViewModel
import com.bertalandancs.otpflickrsearcher.data.model.GetInfoPhoto
import com.bertalandancs.otpflickrsearcher.data.repositories.ImagesRepository
import com.bertalandancs.otpflickrsearcher.data.repositories.InfoResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//SearchStatus and InfoStatus is similar maybe i can clean up (?)
open class InfoStatus {
    data class Ok(val getInfoPhoto: GetInfoPhoto) : InfoStatus()

    data class Fail(val errorCode: Int) : InfoStatus()
    object InternetError : InfoStatus()
    data class UnknownError(val throwable: Throwable) : InfoStatus()
    data class Loading(val isLoading: Boolean) : InfoStatus()
}

class DetailsViewModel(private val imagesRepository: ImagesRepository) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val info = PublishSubject.create<InfoStatus>()
    val infoStatus: Observable<InfoStatus> = info
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getImageInfo(imageId: Long?) {
        disposable.add(
            imagesRepository.getImageById(imageId)
                .doOnSubscribe {
                    info.onNext(InfoStatus.Loading(true))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    info.onNext(InfoStatus.Loading(false))
                    if (it is InfoResponse.StatusOk) {
                        info.onNext(InfoStatus.Ok(it.photoInfoPhoto))
                    } else if (it is InfoResponse.StatusFailed)
                        info.onNext(InfoStatus.Fail(it.errorCode))
                }, {
                    Timber.e("getImageInfo error: $it")
                    info.onNext(InfoStatus.Loading(false))
                    when (it) {
                        is SocketTimeoutException,
                        is UnknownHostException -> info.onNext(InfoStatus.InternetError)
                        else -> info.onNext(InfoStatus.UnknownError(it))
                    }
                })
        )
    }
}