package com.example.lastfmtest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lastfmtest.databinding.ItemTrackBinding
import com.example.lastfmtest.domain.model.AlbumDetailsData
import kotlin.time.Duration.Companion.seconds

class TrackAdapter: RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private val diffUtilsCallback = object : DiffUtil.ItemCallback<AlbumDetailsData.Track>() {
        override fun areItemsTheSame(
            oldItem: AlbumDetailsData.Track,
            newItem: AlbumDetailsData.Track
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: AlbumDetailsData.Track,
            newItem: AlbumDetailsData.Track
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffUtilsCallback)
    var items: List<AlbumDetailsData.Track>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackAdapter.TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemTrackBinding.inflate(inflater, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackAdapter.TrackViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    inner class TrackViewHolder(private val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: AlbumDetailsData.Track) {
            model.apply {
                binding.tvTrackName.text = name
                binding.tvPlace.text = rank.toString()
                if (duration > 0) {
                    binding.tvDuration.isVisible = true
                    binding.tvDuration.text = duration.seconds.toString()
                } else {
                    binding.tvDuration.isVisible = false
                }
            }
        }
    }
}