package dev.mina.flickr_search.ui.viewmodel

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.mina.flickr_search.api.SearchAPI
import dev.mina.flickr_search.data.ErrorState
import dev.mina.flickr_search.data.ImagesResponse
import dev.mina.flickr_search.data.Photo
import dev.mina.flickr_search.data.Photos
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SearchViewModelTest {


    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var searchAPI: SearchAPI
    @Mock
    lateinit var errorObserver: Observer<ErrorState>


    private var viewModel: SearchViewModel? = null
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }

        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel()
        viewModel?.searchAPI = searchAPI

        viewModel?.errorState?.observeForever(errorObserver)
    }

    @Test
    fun searchViewModel_errorState_returnEmptyList() {
        val photos = Photos(
            page = 1,
            pages = 1817,
            perPage = 100,
            total = "181667",
            photo = ArrayList()
        )
        val response = ImagesResponse(photos, "OK")
        val apiResponseTestObserver = viewModel?.apiResponse?.test()
        viewModel?.apiResponse?.onNext(response)
        apiResponseTestObserver?.assertNotTerminated()
            ?.assertNoErrors()
            ?.assertValueCount(1)
        Assert.assertTrue(viewModel?.errorState?.value?.visibility == VISIBLE)
        Assert.assertTrue(viewModel?.errorState?.value?.message == "No Images Found!")
        apiResponseTestObserver?.dispose()
    }

    @Test
    fun searchViewModel_errorState_returnValidList() {
        val photoList = ArrayList<Photo>()
        photoList.add(
            Photo(
                id = "49092579763",
                owner = "21178134@N00",
                secret = "c8cf7cfcd4",
                server = "65535",
                farm = 66,
                title = "Kitten",
                isPublic = 1,
                isFriend = 0,
                isFamily = 0
            )
        )
        val photos = Photos(
            page = 1,
            pages = 1817,
            perPage = 100,
            total = "181667",
            photo = photoList
        )
        val response = ImagesResponse(photos, "OK")
        viewModel?.apiResponse?.onNext(response)
        val apiResponseTestObserver = viewModel?.apiResponse?.test()
        apiResponseTestObserver?.assertNotTerminated()
            ?.assertNoErrors()
            ?.assertValueCount(1)
        Assert.assertTrue(viewModel?.errorState?.value?.visibility == GONE)
        Assert.assertTrue(viewModel?.errorState?.value?.message == "")
        apiResponseTestObserver?.dispose()
    }


    @After
    fun tearDown() {
        viewModel = null
    }

}