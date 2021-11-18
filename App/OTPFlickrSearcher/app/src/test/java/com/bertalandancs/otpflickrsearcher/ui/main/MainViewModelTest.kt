package com.bertalandancs.otpflickrsearcher.ui.main

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bertalandancs.otpflickrsearcher.RxImmediateSchedulerRule
import com.bertalandancs.otpflickrsearcher.data.model.Photos
import com.bertalandancs.otpflickrsearcher.data.model.SearchPhoto
import com.bertalandancs.otpflickrsearcher.data.repositories.ImagesRepository
import com.bertalandancs.otpflickrsearcher.data.repositories.SearchResponse
import com.bertalandancs.otpflickrsearcher.ui.main.model.ThumbnailImage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var sut: MainViewModel
    private lateinit var testObserver: TestObserver<SearchStatus>

    @Rule
    @JvmField
    val schedulers = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var imagesRepository: ImagesRepository

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun beforeEach() {
        `when`(sharedPreferences.edit()).thenReturn(
            sharedPreferencesEditor
        )
        `when`(sharedPreferencesEditor.putString(anyString(), anyString())).thenReturn(
            sharedPreferencesEditor
        )
        sut = MainViewModel(imagesRepository, sharedPreferences)
    }

    @Test
    fun `get images when SearchResponse is StatusOk then post new SearchStatus_Ok with new Thumbnails`() {
        testObserver = TestObserver()
        sut.searchStatus.subscribeWith(testObserver)
        `when`(
            imagesRepository.getImages(
                20, 1, "UTest", "url_n,url_t"
            )
        )
            .thenReturn(
                Observable.just(
                    SearchResponse.StatusOk(Photos().apply {
                        this.page = 1
                        this.photoList = listOf(
                            SearchPhoto().apply {
                                this.id = 1L
                                this.urlT = ""
                                this.urlN = ""
                            }, SearchPhoto().apply {
                                this.id = 2L
                                this.urlT = ""
                                this.urlN = ""
                            })
                    })
                )
            )

        sut.getImages("UTest")

        verify(sharedPreferencesEditor).putString(LAST_SEARCH, "UTest")
        verify(sharedPreferencesEditor).apply()
        testObserver.hasSubscription()
        testObserver.assertValues(
            SearchStatus.Loading(true),
            SearchStatus.Loading(false),
            SearchStatus.Ok(
                listOf(
                    ThumbnailImage(1L, ""),
                    ThumbnailImage(2L, "")
                ), 1
            )
        )
    }

    companion object {
        private const val LAST_SEARCH: String = "LAST_SEARCH"
    }
}