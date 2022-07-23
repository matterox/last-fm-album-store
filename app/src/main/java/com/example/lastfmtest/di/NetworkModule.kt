package com.example.lastfmtest.di

import com.example.lastfmtest.BuildConfig
import com.example.lastfmtest.data.mappers.AlbumDetailsMapper
import com.example.lastfmtest.data.mappers.AlbumMapper
import com.example.lastfmtest.data.mappers.ArtistMapper
import com.example.lastfmtest.data.retrofit.LastFmApiInterceptor
import com.example.lastfmtest.data.retrofit.RequestHandler
import com.example.lastfmtest.data.retrofit.service.LastFmService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("LastFmOkHttp")
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(LastFmApiInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideLastFmApi(
        moshi: Moshi,
        @Named("LastFmOkHttp") okHttpClient: OkHttpClient
    ): LastFmService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.LAST_FM_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LastFmService::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestHandler(moshi: Moshi): RequestHandler {
        return RequestHandler(moshi)
    }

    @Provides
    @Singleton
    fun provideArtistsResponseMapper(): ArtistMapper {
        return ArtistMapper()
    }

    @Provides
    @Singleton
    fun provideAlbumsResponseMapper(): AlbumMapper {
        return AlbumMapper()
    }

    @Provides
    @Singleton
    fun provideAlbumDetailsResponseMapper(): AlbumDetailsMapper {
        return AlbumDetailsMapper()
    }
}