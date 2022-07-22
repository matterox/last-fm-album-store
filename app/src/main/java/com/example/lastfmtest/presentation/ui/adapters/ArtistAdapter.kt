package com.example.lastfmtest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lastfmtest.databinding.ItemArtistBinding
import com.example.lastfmtest.domain.model.ArtistData
import com.example.lastfmtest.presentation.helper.loadAndCacheImage

// TODO: Make base class (SingleTypeAdapter)
class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {
    var onArtistClicked: ((ArtistData) -> Unit)? = null

    private val diffUtilsCallback = object : DiffUtil.ItemCallback<ArtistData>() {
        override fun areItemsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
            return oldItem.mbid == newItem.mbid
        }

        override fun areContentsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffUtilsCallback)
    var items: List<ArtistData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemArtistBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

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