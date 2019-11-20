package dev.mina.flickr_search.data


import com.google.gson.annotations.SerializedName
import dev.mina.flickr_search.data.Photo

data class Photos(
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("perpage")
    val perPage: Int,
    @SerializedName("total")
    val total: String,
    @SerializedName("photo")
    val photo: List<Photo>
)