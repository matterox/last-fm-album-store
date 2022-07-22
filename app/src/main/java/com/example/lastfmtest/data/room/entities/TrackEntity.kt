package com.example.lastfmtest.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["albumMbid", "name"])
data class TrackEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artistName") val artistName: String,
    @ColumnInfo(name = "albumMbid") val albumMbid: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "rank") val rank: Long
)
