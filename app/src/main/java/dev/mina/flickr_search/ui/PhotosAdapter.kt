package dev.mina.flickr_search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.mina.flickr_search.R
import dev.mina.flickr_search.data.Photo
import dev.mina.flickr_search.databinding.PhotoItemBinding


class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.ViewHolder>() {

    private lateinit var photoList: List<Photo>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PhotoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.photo_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return if (::photoList.isInitialized) (photoList.size) else 0
    }

    fun updatePhotoList(photoList: List<Photo>) {
        this.photoList = photoList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.photo = photo
        }

    }


}
