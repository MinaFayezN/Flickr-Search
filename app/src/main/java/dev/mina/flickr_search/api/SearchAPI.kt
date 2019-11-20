package dev.mina.flickr_search.api

import dev.mina.flickr_search.data.ImagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPI {

    @GET("services/rest/")
    fun searchForImages(
        @Query("method")
        method: String = "flickr.photos.search",
        @Query("api_key")
        apiKey: String = "3e7cc266ae2b0e0d78e279ce8e361736",
        @Query("format")
        format: String = "json",
        @Query("nojsoncallback")
        noJSONCallback: Int = 1,
        @Query("safe_search")
        safeSearch: Int = 1,
        @Query("text")
        text: String
    ): Single<ImagesResponse>
}