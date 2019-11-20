package dev.mina.flickr_search.ui.viewmodel

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import dev.mina.flickr_search.api.SearchAPI
import dev.mina.flickr_search.base.BaseViewModel
import dev.mina.flickr_search.data.ErrorState
import dev.mina.flickr_search.data.ImagesResponse
import dev.mina.flickr_search.data.Photo
import dev.mina.flickr_search.ui.PhotoListAdapter
import dev.mina.flickr_search.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

const val TAG = "FlickrSearch"

class SearchViewModel : BaseViewModel() {

    @Inject
    lateinit var searchAPI: SearchAPI

    val searchText = BehaviorSubject.create<String>()
    val errorState = MutableLiveData<ErrorState>()
    val loadingVisibility = MutableLiveData<Int>(GONE)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val apiResponse = BehaviorSubject.create<ImagesResponse>()
    val photoListSubject = BehaviorSubject.create<List<Photo>>()
    private val disposables = CompositeDisposable()


    val columnNumbers = 3


    init {
        searchText
            .subscribe(
                { text ->
                    if ((text.isNotEmpty())) {
                        loadingVisibility.value = VISIBLE
                        errorState.value = ErrorState()
                        searchAPI.searchForImages(text = text)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response ->
                                apiResponse.onNext(response)
                            }, { throwable ->
                                Log.e(TAG, "Error while calling SearchAPI: $throwable")
                                errorState.value =
                                    ErrorState(
                                        message = throwable.message ?: throwable.toString(),
                                        visibility = VISIBLE
                                    )
                                loadingVisibility.value = GONE
                            })
                    }
                }, { throwable ->
                    Log.e(TAG, "Error in subscription for searchText:  $throwable")
                    errorState.value =
                        ErrorState(
                            message = throwable.message ?: throwable.toString(),
                            visibility = VISIBLE
                        )
                    loadingVisibility.value = GONE
                }
            ).addTo(disposables)
        apiResponse
            .subscribe(
                { response ->
                    loadingVisibility.value = GONE
                    if (response.photos.photo.isNotEmpty()) {
                        errorState.value = ErrorState()
                        photoListSubject.onNext(response.photos.photo)
                    } else {
                        errorState.value =
                            ErrorState(
                                message = "No Images Found!",
                                visibility = VISIBLE
                            )
                    }
                },
                { throwable ->
                    loadingVisibility.value = GONE
                    errorState.value =
                        ErrorState(
                            message = throwable.message ?: throwable.toString(),
                            visibility = VISIBLE
                        )
                    Log.e(TAG, "Error in subscription for apiResponse:  $throwable")
                }
            ).addTo(disposables)
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}