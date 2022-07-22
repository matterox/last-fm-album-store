package com.example.lastfmtest.data.retrofit.service

import com.example.lastfmtest.data.model.AlbumDetailsResponse
import com.example.lastfmtest.data.model.ArtistAlbumsResponse
import com.example.lastfmtest.data.model.ArtistDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {
    @GET("?method=artist.search")
    suspend fun searchArtist(@Query("artist") query: String): Response<ArtistDataResponse>

    @GET("?method=artist.gettopalbums")
    suspend fun getTopAlbums(@Query("mbid") artistId: String): Response<ArtistAlbumsResponse>

    @GET("?method=album.getinfo")
    suspend fun getAlbumDetails(@Query("mbid") albumId: String): Response<AlbumDetailsResponse>
}