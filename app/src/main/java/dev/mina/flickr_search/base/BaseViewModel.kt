package dev.mina.flickr_search.base

import androidx.lifecycle.ViewModel
import dev.mina.flickr_search.ui.viewmodel.SearchViewModel
import dev.mina.flickr_search.injection.component.DaggerViewModelInjector
import dev.mina.flickr_search.injection.component.ViewModelInjector
import dev.mina.flickr_search.injection.module.NetworkModule


open class BaseViewModel : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }


    private fun inject() {
        when (this) {
            is SearchViewModel -> injector.inject(this)
        }
    }
}