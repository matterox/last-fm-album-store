package com.example.lastfmtest.presentation.ui.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.lastfmtest.domain.model.ArtistData

class ArtistDiffUtil : DiffUtil.ItemCallback<ArtistData>() {
    override fun areItemsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
        return oldItem.mbid == newItem.mbid
    }

    override fun areContentsTheSame(oldItem: ArtistData, newItem: ArtistData): Boolean {
        return oldItem == newItem
    }

}