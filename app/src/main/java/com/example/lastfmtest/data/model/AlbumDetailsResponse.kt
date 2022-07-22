package com.example.lastfmtest.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumDetailsResponse(
    @Json(name = "album")
    val album: Album?
) {
    @JsonClass(generateAdapter = true)
    data class Album(
        @Json(name = "artist")
        val artist: String?,
        @Json(name = "image")
        val image: List<Image>?,
        @Json(name = "listeners")
        val listeners: Long?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "playcount")
        val playcount: Long?,
        @Json(name = "tags")
        val tags: Tags?,
        @Json(name = "tracks")
        val tracks: Tracks?,
        @Json(name = "url")
        val url: String?,
        @Json(name = "wiki")
        val wiki: Wiki?
    ) {
        @JsonClass(generateAdapter = true)
        data class Image(
            @Json(name = "size")
            val size: String?,
            @Json(name = "#text")
            val text: String?
        )

        @JsonClass(generateAdapter = true)
        data class Tags(
            @Json(name = "tag")
            val tag: List<Tag>?
        ) {
            @JsonClass(generateAdapter = true)
            data class Tag(
                @Json(name = "name")
                val name: String?,
                @Json(name = "url")
                val url: String?
            )
        }

        @JsonClass(generateAdapter = true)
        data class Tracks(
            @Json(name = "track")
            val track: List<Track>?
        ) {
            @JsonClass(generateAdapter = true)
            data class Track(
                @Json(name = "artist")
                val artist: Artist?,
                @Json(name = "@attr")
                val attr: Attr?,
                @Json(name = "duration")
                val duration: Long?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "url")
                val url: String?
            ) {
                @JsonClass(generateAdapter = true)
                data class Artist(
                    @Json(name = "mbid")
                    val mbid: String?,
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "url")
                    val url: String?
                )

                @JsonClass(generateAdapter = true)
                data class Attr(
                    @Json(name = "rank")
                    val rank: Long?
                )
            }
        }

        @JsonClass(generateAdapter = true)
        data class Wiki(
            @Json(name = "content")
            val content: String?,
            @Json(name = "published")
            val published: String?,
            @Json(name = "summary")
            val summary: String?
        )
    }
}