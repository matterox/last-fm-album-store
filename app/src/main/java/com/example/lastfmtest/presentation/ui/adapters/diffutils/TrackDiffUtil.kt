package com.example.lastfmtest.presentation.ui.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.lastfmtest.domain.model.AlbumDetailsData

class TrackDiffUtil : DiffUtil.ItemCallback<AlbumDetailsData.Track>() {
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