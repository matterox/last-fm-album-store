package com.example.lastfmtest.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["albumMbid"])
data class AlbumDetailsEntity(
    @ColumnInfo(name = "albumMbid") val mbid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "tags") val tags: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "plays") val plays: Long,
)