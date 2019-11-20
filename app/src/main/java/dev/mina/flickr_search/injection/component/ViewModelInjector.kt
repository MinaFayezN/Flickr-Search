package dev.mina.flickr_search.injection.component

import dagger.Component
import dev.mina.flickr_search.ui.viewmodel.SearchViewModel
import dev.mina.flickr_search.injection.module.NetworkModule
import javax.inject.Singleton


@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {


    fun inject(searchViewModel: SearchViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}