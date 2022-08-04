package com.example.lastfmtest.data.repository

import com.example.lastfmtest.data.mappers.AlbumDetailsMapper
import com.example.lastfmtest.data.mappers.AlbumMapper
import com.example.lastfmtest.data.mappers.ArtistMapper
import com.example.lastfmtest.data.retrofit.RequestHandler
import com.example.lastfmtest.presentation.helper.EitherResult
import com.example.lastfmtest.data.retrofit.service.LastFmService
import com.example.lastfmtest.data.room.AlbumDao
import com.example.lastfmtest.domain.model.*
import com.example.lastfmtest.domain.repository.ArtistsRepository
import com.example.lastfmtest.presentation.helper.EiterResultFlow
import kotlinx.coroutines.flow.*

class ArtistsRepositoryImpl(
    private val api: LastFmService,
    private val requestHandler: RequestHandler,
    private val artistMapper: ArtistMapper,
    private val albumMapper: AlbumMapper,
    private val albumDetailsMapper: AlbumDetailsMapper,
    private val albumDao: AlbumDao
): ArtistsRepository {

    override suspend fun searchArtist(query: String): EitherResult<DefaultDomainError, List<ArtistData>> {
        return requestHandler.safeRequest(
            response = { api.searchArtist(query) },
            successTransform = artistMapper::fromResponse
        )
    }

    override suspend fun getTopAlbums(artistId: String): EitherResult<DefaultDomainError, List<AlbumData>> {
        return requestHandler.safeRequest(
            response = { api.getTopAlbums(artistId) },
            successTransform = { result -> albumMapper.fromResponse(result) }
        )
    }

    override fun observeAlbumDetails(albumId: String): EiterResultFlow<DefaultDomainError, AlbumDetailsData> {
        return flow {
            val cacheEntries = albumDao.getDetails(albumId)
            val cachedAlbumDetailsEntity = cacheEntries.firstOrNull()
            val cachedAlbumDetails: AlbumDetailsData = albumDetailsMapper.fromEntity(cachedAlbumDetailsEntity)
            // First, we emit data from cache.
            if (cachedAlbumDetails != AlbumDetailsData.EMPTY)
                emit(EitherResult.Success(cachedAlbumDetails))

            val apiResult = requestHandler.safeRequest(
                response = { api.getAlbumDetails(albumId) },
                successTransform = { result -> albumDetailsMapper.fromResponse(result, albumId) }
            )
            // Saving albums to cache even if they were not liked.
            // TODO: Need to make cache cleaning option
            if (apiResult is EitherResult.Success) {
                val entitiesMap = albumDetailsMapper.toEntity(apiResult.data).entries.first()
                albumDao.insertAllAlbumDetails(entitiesMap)
            }
            // We can update cache silently, so next time user visits page, they see updated data
            emit(apiResult)
        }
    }

    override suspend fun observeSavedAlbums(): Flow<List<AlbumData>> {
        return albumDao.observeFavorites()
            .map { savedAlbums ->
                savedAlbums.map { album -> albumMapper.fromEntity(album) }
            }
    }

    override suspend fun saveAlbum(albumData: AlbumData): EitherResult<DefaultDomainError, Unit> {
        albumDao.insertFavorite(albumMapper.toEntity(albumData.copy(isSaved = true)))

        // Caching album details for offline
        val albumDetailsRemoteResult = requestHandler.safeRequest(
            response = { api.getAlbumDetails(albumData.mbid) },
            successTransform = { result -> albumDetailsMapper.fromResponse(result, albumData.mbid) }
        )
        // TODO: Maybe make it silent, without result
        return when (albumDetailsRemoteResult) {
            is EitherResult.Success -> {
                albumDetailsRemoteResult.data.let { albumDetails ->
                    val entitiesMap = albumDetailsMapper.toEntity(albumDetails).entries.first()
                    albumDao.insertAllAlbumDetails(entitiesMap)
                }
                EitherResult.Success(Unit)
            }
            is EitherResult.Failure -> {
                albumDetailsRemoteResult
            }
        }
    }

    override suspend fun addToFavorites(albumId: String) {
        albumDao.updateFavorite(albumId, true)
    }

    override suspend fun removeFromFavorites(albumId: String) {
        albumDao.updateFavorite(albumId, false)
    }

    override suspend fun deleteAlbum(albumId: String) {
        albumDao.deleteFavoriteAndDetails(albumId)
    }
}