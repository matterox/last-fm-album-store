package com.example.lastfmtest.domain.model

data class AlbumDetailsData(
    val name: String,
    val artist: String,
    val cover: String,
    val mbid: String,
    val tags: List<String>,
    val summary: String,
    val plays: Long,
    val tracks: List<Track>
) {
    data class Track(
        val name: String,
        val duration: Long,
        val url: String,
        val rank: Long
    )

    companion object {
        val EMPTY = AlbumDetailsData(
            name = "",
            artist = "",
            cover = "",
            mbid = "",
            tags = emptyList(),
            summary = "",
            plays = 0,
            tracks = emptyList()
        )
    }
}
