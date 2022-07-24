package com.example.lastfmtest.data.mappers

import com.example.lastfmtest.data.model.AlbumDetailsResponse
import com.example.lastfmtest.data.room.entities.AlbumDetailsEntity
import com.example.lastfmtest.data.room.entities.AlbumDetailsWithTracks
import com.example.lastfmtest.data.room.entities.TrackEntity
import com.example.lastfmtest.domain.model.AlbumDetailsData

class AlbumDetailsMapper {
    fun fromResponse(model: AlbumDetailsResponse, albumId: String): AlbumDetailsData {
        return model.album?.let {
            AlbumDetailsData(
                name = it.name ?: "",
                artist = it.artist ?: "",
                cover = MapperHelper.albumImageFromMbid(albumId),
                mbid = albumId,
                tags = it.tags?.tag?.map { tag -> tag.name ?: "" } ?: emptyList(),
                summary = it.wiki?.summary ?: "",
                plays = it.playcount ?: 0L,
                tracks = it.tracks?.track?.map { track ->
                    AlbumDetailsData.Track(
                        name = track.name ?: "",
                        artist = track.artist?.name ?: it.artist ?: "",
                        duration = track.duration ?: 0L,
                        url = track.url ?: "",
                        rank = track.attr?.rank ?: -1
                    )
                } ?: emptyList()
            )
        } ?: AlbumDetailsData.EMPTY
    }

    fun toEntity(album: AlbumDetailsData): Map<AlbumDetailsEntity, List<TrackEntity>> {
        val albumEntity = AlbumDetailsEntity(
            mbid = album.mbid,
            name = album.name,
            artist = album.artist,
            tags = album.tags.joinToString(","),
            summary = album.summary,
            plays = album.plays
        )
        val trackEntities = album.tracks.map { track ->
            TrackEntity(
                name = track.name,
                artistName = track.artist,
                albumMbid = album.mbid,
                url = track.url,
                duration = track.duration,
                rank = track.rank
            )
        }
        return mapOf(albumEntity to trackEntities)
    }

    fun fromEntity(albumDetailsWithTracks: AlbumDetailsWithTracks?): AlbumDetailsData {
        return albumDetailsWithTracks?.let { albumAndTracks ->
            val album = albumAndTracks.albumDetailsEntity
            val tracks = albumAndTracks.tracksSortedByRank
            AlbumDetailsData(
                name = album.name,
                artist = album.artist,
                cover = MapperHelper.albumImageFromMbid(album.mbid),
                mbid = album.mbid,
                tags = album.tags.split(","),
                summary = album.summary,
                plays = album.plays,
                tracks = tracks.map { track ->
                    AlbumDetailsData.Track(
                        name = track.name,
                        artist = track.artistName,
                        duration = track.duration,
                        url = track.url,
                        rank = track.rank
                    )
                }
            )
        } ?: AlbumDetailsData.EMPTY
    }
}