package dev.mina.flickr_search.utils

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.mina.flickr_search.R
import dev.mina.flickr_search.data.ErrorState

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("columnNumbers")
fun setColumnNumbers(view: RecyclerView, number: Int) {
    view.layoutManager = GridLayoutManager(view.context, number)
}

@BindingAdapter("glideURL")
fun setGlideURL(view: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .error(R.drawable.ic_loading)
        .into(view)
}

@BindingAdapter("loadingVisibility")
fun setLoadingVisibility(view: ProgressBar, visibilitySubject: MutableLiveData<Int>) {
    visibilitySubject.value?.let { visibility ->
        view.visibility = visibility
    }
}

@BindingAdapter("state")
fun setState(view: TextView, state: MutableLiveData<ErrorState>) {
    state.value?.let { errorState ->
        view.visibility = errorState.visibility
        view.text = errorState.message
    }
}