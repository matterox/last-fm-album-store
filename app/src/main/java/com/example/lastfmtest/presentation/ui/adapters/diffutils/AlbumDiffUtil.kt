package com.example.lastfmtest.presentation.ui.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.lastfmtest.domain.model.AlbumData

class AlbumDiffUtil : DiffUtil.ItemCallback<AlbumData>() {
    override fun areItemsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
        return oldItem.mbid == newItem.mbid
    }

    override fun areContentsTheSame(oldItem: AlbumData, newItem: AlbumData): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: AlbumData, newItem: AlbumData): Any? {
        if (oldItem.isSaved != newItem.isSaved) return newItem.isSaved
        return super.getChangePayload(oldItem, newItem)
    }
}