package dev.mina.flickr_search.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.widget.editorActionEvents
import dev.mina.flickr_search.R
import dev.mina.flickr_search.data.ErrorState
import dev.mina.flickr_search.databinding.ActivitySearchBinding
import dev.mina.flickr_search.ui.viewmodel.SearchViewModel
import dev.mina.flickr_search.ui.viewmodel.TAG
import dev.mina.flickr_search.utils.addTo
import dev.mina.flickr_search.utils.hideKeyboard
import io.reactivex.disposables.CompositeDisposable

class SearchActivity : AppCompatActivity() {


    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private val photoListAdapter = PhotoListAdapter()
    private val activityDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.adapter = photoListAdapter

        subscribeToPhotoListSubject()
        subscribeToSearchTextActionEvent()
    }

    private fun subscribeToPhotoListSubject() {
        viewModel.photoListSubject.subscribe(
            { list ->
                photoListAdapter.updatePhotoList(list)
            },
            { throwable ->
                viewModel.loadingVisibility.value = View.GONE
                viewModel.errorState.value =
                    ErrorState(
                        message = throwable.message ?: throwable.toString(),
                        visibility = View.VISIBLE
                    )
                Log.e(TAG, "Error in subscription for photoList:  $throwable")
            }
        ).addTo(activityDisposables)
    }

    private fun subscribeToSearchTextActionEvent() {
        binding
            .searchEditText
            .editorActionEvents { event ->
                event.actionId == EditorInfo.IME_ACTION_SEARCH
            }
            .filter { action -> action.actionId == EditorInfo.IME_ACTION_SEARCH }
            .subscribe(
                { event ->
                    event.view.clearFocus()
                    hideKeyboard()
                    viewModel.searchText.onNext(event.view.text.toString())
                },
                { throwable ->
                    Log.e(TAG, "Error in SearchTextActionEvent: $throwable")
                    viewModel.errorState.value =
                        ErrorState(
                            message = throwable.message ?: throwable.toString(),
                            visibility = View.VISIBLE
                        )
                }
            )
            .addTo(activityDisposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityDisposables.dispose()
    }
}
