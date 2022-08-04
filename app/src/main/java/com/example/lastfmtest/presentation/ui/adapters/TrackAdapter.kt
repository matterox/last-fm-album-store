package com.example.lastfmtest.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lastfmtest.databinding.ItemTrackBinding
import com.example.lastfmtest.domain.model.AlbumDetailsData
import com.example.lastfmtest.presentation.ui.adapters.diffutils.TrackDiffUtil
import kotlin.time.Duration.Companion.seconds

class TrackAdapter: ListAdapter<AlbumDetailsData.Track, TrackAdapter.TrackViewHolder>(TrackDiffUtil()) {
    var items: List<AlbumDetailsData.Track>
        get() = currentList
        set(value) {
            submitList(value)
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
        holder.bind(getItem(position))
    }

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