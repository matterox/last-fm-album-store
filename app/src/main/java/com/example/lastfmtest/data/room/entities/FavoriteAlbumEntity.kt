package com.example.lastfmtest.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["albumMbid"])
data class FavoriteAlbumEntity(
    @ColumnInfo(name = "albumMbid") val mbid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artistMbid") val artistMbid: String,
    @ColumnInfo(name = "artistName") val artistName: String,
    @ColumnInfo(name = "plays") val plays: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Date,
)
