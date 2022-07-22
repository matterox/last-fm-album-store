package com.example.lastfmtest.data.room

import androidx.room.*
import com.example.lastfmtest.data.room.entities.AlbumDetailsEntity
import com.example.lastfmtest.data.room.entities.AlbumDetailsWithTracks
import com.example.lastfmtest.data.room.entities.FavoriteAlbumEntity
import com.example.lastfmtest.data.room.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Transaction
    suspend fun insertAllAlbumDetails(detailsEntry: Map.Entry<AlbumDetailsEntity, List<TrackEntity>>) {
        insertAlbumDetails(detailsEntry.key)
        insertTracks(detailsEntry.value)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(albumId: FavoriteAlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumDetails(album: AlbumDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Query("SELECT * FROM favoritealbumentity")
    fun observeFavorites(): Flow<List<FavoriteAlbumEntity>>

    @Transaction
    @Query("SELECT * FROM albumdetailsentity WHERE albumMbid = :albumId")
    suspend fun getDetails(albumId: String): List<AlbumDetailsWithTracks>

    @Query("DELETE FROM trackentity WHERE albumMbid = :albumId")
    suspend fun deleteAlbumTracks(albumId: String)

    @Query("DELETE FROM albumdetailsentity WHERE albumMbid = :albumId")
    suspend fun delete(albumId: String)

    @Query("DELETE FROM favoritealbumentity WHERE albumMbid = :albumId")
    suspend fun deleteFavorite(albumId: String)

    @Transaction
    suspend fun deleteFavoriteAndDetails(albumId: String) {
        deleteAlbumTracks(albumId)
        delete(albumId)
        deleteFavorite(albumId)
    }
}