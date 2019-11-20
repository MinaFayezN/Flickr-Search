package dev.mina.flickr_search.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


fun Activity.hideKeyboard() {
    this.currentFocus?.let { view ->
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, HIDE_NOT_ALWAYS)
    }
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}