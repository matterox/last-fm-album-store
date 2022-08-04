package com.example.lastfmtest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lastfmtest.databinding.ItemArtistBinding
import com.example.lastfmtest.domain.model.ArtistData
import com.example.lastfmtest.presentation.helper.loadAndCacheImage
import com.example.lastfmtest.presentation.ui.adapters.diffutils.ArtistDiffUtil

class ArtistAdapter: ListAdapter<ArtistData, ArtistAdapter.ArtistViewHolder>(ArtistDiffUtil()) {
    var onArtistClicked: ((ArtistData) -> Unit)? = null

    var items: List<ArtistData>
        get() = currentList
        set(value) {
            submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemArtistBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArtistViewHolder(private val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ArtistData) {
            model.apply {
                binding.imageView.loadAndCacheImage(
                    binding.root.context,
                    image,
                    onResourceReady = {
                        binding.pbArtistImage.isVisible = false
                    },
                    onLoadFailed = {
                        binding.pbArtistImage.isVisible = false
                    }
                )
                binding.tvArtistName.text = name
                binding.cvArtist.setOnClickListener { onArtistClicked?.invoke(this) }
                binding.ivNoData.isVisible = mbid.isBlank()
            }
        }
    }
}