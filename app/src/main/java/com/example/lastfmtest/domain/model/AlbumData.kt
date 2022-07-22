package com.example.lastfmtest.domain.model

data class AlbumData(
    val mbid: String,
    val name: String,
    val artistMbid: String,
    val artistName: String,
    val playCount: Long,
    val image: String,
    val isSaved: Boolean
)