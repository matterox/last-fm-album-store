package com.example.lastfmtest.domain.repository

import com.example.lastfmtest.domain.model.*
import com.example.lastfmtest.presentation.helper.EitherResult
import com.example.lastfmtest.presentation.helper.EiterResultFlow
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {
    suspend fun searchArtist(query: String): EitherResult<DefaultDomainError, List<ArtistData>>
    suspend fun getTopAlbums(artistId: String): EitherResult<DefaultDomainError, List<AlbumData>>
    fun observeAlbumDetails(albumId: String): EiterResultFlow<DefaultDomainError, AlbumDetailsData>
    suspend fun observeSavedAlbums(): Flow<List<AlbumData>>
    suspend fun saveAlbum(albumData: AlbumData): EitherResult<DefaultDomainError, Unit>
    suspend fun addToFavorites(albumId: String)
    suspend fun removeFromFavorites(albumId: String)
    suspend fun deleteAlbum(albumId: String)
}