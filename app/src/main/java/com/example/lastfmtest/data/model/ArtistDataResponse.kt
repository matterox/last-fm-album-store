package com.example.lastfmtest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistDataResponse(
    @Json(name = "results") val results: ArtistsResult?
) {
    @JsonClass(generateAdapter = true)
    data class ArtistsResult(
        @Json(name = "opensearch:totalResults") val totalResults: Int?,
        @Json(name = "opensearch:startIndex") val startIndex: Int?,
        @Json(name = "opensearch:itemsPerPage") val itemsPerPage: Int?,
        @Json(name = "artistmatches") val artistMatches: Artists?
    ) {
        @JsonClass(generateAdapter = true)
        data class Artists(
            @Json(name = "artist") val artists: List<Artist>?
        ) {
            @JsonClass(generateAdapter = true)
            data class Artist(
                @Json(name = "name") val name: String?,
                @Json(name = "listeners") val listeners: Long?,
                @Json(name = "mbid") val mbid: String?,
                @Json(name = "url") val url: String?,
                @Json(name = "image") val image: List<ImageData>?,
                @Json(name = "streamable") val streamable: Int?
            ) {
                @JsonClass(generateAdapter = true)
                data class ImageData(
                    @Json(name = "#text") val url: String?,
                    @Json(name = "size") val size: String?
                )
            }
        }
    }
}