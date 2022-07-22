package com.example.lastfmtest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistAlbumsResponse(
    @Json(name = "topalbums")
    val topalbums: Topalbums?
) {
    @JsonClass(generateAdapter = true)
    data class Topalbums(
        @Json(name = "album")
        val album: List<Album>?,
        @Json(name = "@attr")
        val attr: Attr?
    ) {
        @JsonClass(generateAdapter = true)
        data class Album(
            @Json(name = "artist")
            val artist: Artist?,
            @Json(name = "image")
            val image: List<Image>?,
            @Json(name = "mbid")
            val mbid: String?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "playcount")
            val playcount: Long?,
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
            data class Image(
                @Json(name = "size")
                val size: String?,
                @Json(name = "#text")
                val text: String?
            )
        }

        @JsonClass(generateAdapter = true)
        data class Attr(
            @Json(name = "artist")
            val artist: String?,
            @Json(name = "page")
            val page: String?,
            @Json(name = "perPage")
            val perPage: String?,
            @Json(name = "total")
            val total: String?,
            @Json(name = "totalPages")
            val totalPages: String?
        )
    }
}