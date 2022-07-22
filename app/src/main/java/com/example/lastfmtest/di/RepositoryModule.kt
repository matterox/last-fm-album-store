package com.example.lastfmtest.di

import com.example.lastfmtest.data.mappers.AlbumDetailsMapper
import com.example.lastfmtest.data.mappers.AlbumMapper
import com.example.lastfmtest.data.mappers.ArtistMapper
import com.example.lastfmtest.data.repository.ArtistsRepositoryImpl
import com.example.lastfmtest.data.retrofit.RequestHandler
import com.example.lastfmtest.data.retrofit.service.LastFmService
import com.example.lastfmtest.data.room.AlbumDao
import com.example.lastfmtest.domain.repository.ArtistsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideArtistsRepository(
        lastFmService: LastFmService,
        requestHandler: RequestHandler,
        artistsMapper: ArtistMapper,
        albumsMapper: AlbumMapper,
        albumDetailsMapper: AlbumDetailsMapper,
        albumDao: AlbumDao
    ): ArtistsRepository {
        return ArtistsRepositoryImpl(
            lastFmService,
            requestHandler,
            artistsMapper,
            albumsMapper,
            albumDetailsMapper,
            albumDao
        )
    }
}