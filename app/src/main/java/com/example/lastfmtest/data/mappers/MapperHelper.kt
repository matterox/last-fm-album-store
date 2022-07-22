package com.example.lastfmtest.data.mappers

object MapperHelper {
    fun albumImageFromMbid(mbid: String): String {
        return "https://coverartarchive.org/release/${mbid}/front"
    }
}