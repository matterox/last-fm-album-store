package com.example.lastfmtest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lastfmtest.R
import com.example.lastfmtest.databinding.ItemAlbumBinding
import com.example.lastfmtest.domain.model.AlbumData
import com.example.lastfmtest.presentation.helper.loadAndCacheImage
import java.text.NumberFormat
import java.util.*

// TODO: Make base class (SingleTypeAdapter)
class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    var onAlbumClicked: ((AlbumData) -> Unit)? = null
    var onFavoriteClicked: ((AlbumData) -> Unit)? = null

    private val diffUtilsCallback = object : DiffUtil.ItemCallback<AlbumData>() {
        override fun areItemsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
            return oldItem.mbid == newItem.mbid
        }

        override fun areContentsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffUtilsCallback)
    var items: List<AlbumData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemAlbumBinding.inflate(inflater, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    inner class AlbumViewHolder(private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: AlbumData) {
            model.apply {
                binding.pbCoverLoading.isVisible = true
                binding.imageView.loadAndCacheImage(
                    binding.root.context,
                    image,
                    onResourceReady = {
                        binding.pbCoverLoading.isVisible = false
                    },
                    onLoadFailed = {
                        binding.pbCoverLoading.isVisible = false
                    }
                )

                binding.tvAlbumName.text = name
                binding.tvArtists.text = binding.root.context.getString(R.string.artist_label, artistName)
                binding.tvPlays.text = NumberFormat.getInstance(Locale.getDefault()).format(playCount)
                binding.cvAlbum.setOnClickListener { onAlbumClicked?.invoke(this) }
                binding.ivSave.setOnClickListener { onFavoriteClicked?.invoke(this) }
                binding.ivSave.isVisible = mbid.isNotBlank()
                binding.ivSave.setLiked(isSaved)
            }
        }
    }
}