package com.example.lastfmtest.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lastfmtest.data.room.converters.Converters
import com.example.lastfmtest.data.room.entities.AlbumDetailsEntity
import com.example.lastfmtest.data.room.entities.FavoriteAlbumEntity
import com.example.lastfmtest.data.room.entities.TrackEntity

@Database(
    entities = [
        FavoriteAlbumEntity::class,
        AlbumDetailsEntity::class,
        TrackEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}