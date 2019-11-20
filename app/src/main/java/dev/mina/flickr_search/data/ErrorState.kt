package dev.mina.flickr_search.data

import android.view.View.GONE

data class ErrorState(
    val message: String = "",
    val visibility: Int = GONE
)
