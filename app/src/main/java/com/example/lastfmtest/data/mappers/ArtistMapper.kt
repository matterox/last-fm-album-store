package com.example.lastfmtest.data.mappers

import com.example.lastfmtest.data.model.ArtistDataResponse
import com.example.lastfmtest.domain.model.ArtistData

class ArtistMapper {
    fun fromResponse(model: ArtistDataResponse): List<ArtistData> {
        return model.results?.artistMatches?.artists?.map { artistData ->
            artistData.let { artist ->
                ArtistData(
                    name = artist.name ?: "",
                    mbid = artist.mbid ?: "",
                    image = artist.image?.first { it.size == "medium" }?.url ?: "",
                    listeners = artist.listeners ?: 0
                )
            }
        } ?: listOf()
    }
}