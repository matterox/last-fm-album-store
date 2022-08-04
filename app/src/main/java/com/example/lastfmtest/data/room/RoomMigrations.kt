package com.example.lastfmtest.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object RoomMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE new_FavoriteAlbumEntity (
                    albumMbid TEXT NOT NULL,
                    name TEXT NOT NULL,
                    artistMbid TEXT NOT NULL,
                    artistName TEXT NOT NULL,
                    plays INTEGER NOT NULL,
                    timestamp INTEGER NOT NULL,
                    isFavorite INTEGER NOT NULL, 
                    PRIMARY KEY(`albumMbid`)
                )
                """.trimIndent())
            database.execSQL("""
                INSERT INTO new_FavoriteAlbumEntity (albumMbid, name, artistMbid, artistName, plays, timestamp, isFavorite)
                SELECT albumMbid, name, artistMbid, artistName, plays, timestamp, 1 FROM FavoriteAlbumEntity
                """.trimIndent())
            database.execSQL("DROP TABLE FavoriteAlbumEntity")
            database.execSQL("ALTER TABLE new_FavoriteAlbumEntity RENAME TO FavoriteAlbumEntity")
        }
    }
}