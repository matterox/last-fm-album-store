package com.example.lastfmtest.data.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumDetailsWithTracks(
    @Embedded val albumDetailsEntity: AlbumDetailsEntity,
    @Relation(
        parentColumn = "albumMbid",
        entityColumn = "albumMbid"
    )
    val tracks: List<TrackEntity>
) {
    val tracksSortedByRank: List<TrackEntity>
        get() {
            return tracks.sortedBy { it.rank }
        }
}