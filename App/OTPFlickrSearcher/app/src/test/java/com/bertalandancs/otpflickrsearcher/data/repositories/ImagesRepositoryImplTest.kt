package com.bertalandancs.otpflickrsearcher.data.repositories

import com.bertalandancs.otpflickrsearcher.data.api.FlickrService
import com.bertalandancs.otpflickrsearcher.data.model.Err
import com.bertalandancs.otpflickrsearcher.data.model.Photos
import com.bertalandancs.otpflickrsearcher.data.model.SearchPhoto
import com.bertalandancs.otpflickrsearcher.data.model.SearchRsp
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ImagesRepositoryImplTest {

    private lateinit var sut: ImagesRepository
    private lateinit var testObserver: TestObserver<SearchResponse>

    @Mock
    lateinit var flickrService: FlickrService

    @Before
    fun beforeEach() {
        sut = ImagesRepositoryImpl(flickrService)
    }

    @Test
    fun `Get images when response status is OK then post new SearchResponse_StatusOk state with photos`() {
        testObserver = TestObserver()
        `when`(flickrService.getImages(text = "UTest")).thenReturn(
            Observable.just(SearchRsp().apply {
                this.stat = "ok"
                this.error = Err()
                this.photos = Photos().apply {
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
                }
            })
        )

        sut.getImages(text = "UTest").subscribeWith(testObserver)

        testObserver.hasSubscription()
        testObserver.assertValue {
            it as SearchResponse.StatusOk
            it.photos.photoList.size == 2
                    && it.photos.photoList[0].id == 1L
                    && it.photos.photoList[1].id == 2L
        }
    }

    @Test
    fun `Get images when response status is not OK then post new SearchResponse_StatusFailed state with error code`() {
        testObserver = TestObserver()
        `when`(flickrService.getImages(text = "UTest")).thenReturn(
            Observable.just(SearchRsp().apply {
                this.stat = "fail"
                this.error = Err().apply {
                    this.code = 999
                    this.msg = ""
                }
            })
        )

        sut.getImages(text = "UTest").subscribeWith(testObserver)

        testObserver.hasSubscription()
        testObserver.assertValue {
            it as SearchResponse.StatusFailed
            it.errorCode == 999
        }
    }


}