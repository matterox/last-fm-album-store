package com.example.lastfmtest.data.mappers

import com.example.lastfmtest.data.model.ArtistAlbumsResponse
import com.example.lastfmtest.data.room.entities.FavoriteAlbumEntity
import com.example.lastfmtest.domain.model.AlbumData
import java.util.*

class AlbumMapper {
    fun fromResponse(albums: ArtistAlbumsResponse): List<AlbumData> {
        return albums.topalbums?.album?.map { album ->
            AlbumData(
                mbid = album.mbid ?: "",
                name = album.name ?: "",
                playCount = album.playcount ?: 0L,
                artistMbid = album.artist?.mbid ?: "",
                artistName = album.artist?.name ?: "",
                image = album.mbid?.run { MapperHelper.albumImageFromMbid(this) }
                    ?: "",
                isSaved = false
            )
        } ?: emptyList()
    }

    fun toEntity(albumData: AlbumData): FavoriteAlbumEntity {
        return FavoriteAlbumEntity(
            mbid = albumData.mbid,
            name = albumData.name,
            artistMbid = albumData.artistMbid,
            artistName = albumData.name,
            plays = albumData.playCount,
            timestamp = Date()
        )
    }

    fun fromEntity(entity: FavoriteAlbumEntity): AlbumData {
        return AlbumData(
            mbid = entity.mbid,
            name = entity.name,
            playCount = entity.plays,
            artistMbid = entity.artistMbid,
            artistName = entity.artistName,
            image = MapperHelper.albumImageFromMbid(entity.mbid),
            isSaved = true
        )
    }
}