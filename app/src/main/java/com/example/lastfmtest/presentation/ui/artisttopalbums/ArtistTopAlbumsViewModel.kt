package com.example.lastfmtest.presentation.ui.artisttopalbums

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lastfmtest.R
import com.example.lastfmtest.domain.model.AlbumData
import com.example.lastfmtest.presentation.helper.navigation.MbidNavigationData
import com.example.lastfmtest.domain.repository.ArtistsRepository
import com.example.lastfmtest.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistTopAlbumsViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val resources: Resources
) : BaseViewModel() {
    lateinit var artistMbid: String
    lateinit var artistName: String

    private val _artistNameLiveData = MutableLiveData<String>()
    val artistNameLiveData: LiveData<String> = _artistNameLiveData
    private val _albumsLiveData = MutableLiveData<List<AlbumData>>()
    val albumsLiveData: LiveData<List<AlbumData>> = _albumsLiveData
    private val _navigationAlbumFlow = Channel<MbidNavigationData>()
    val navigationAlbumFlow: Flow<MbidNavigationData> = _navigationAlbumFlow.receiveAsFlow()

    override fun start() {
        super.start()
        _artistNameLiveData.value = artistName
        _progressLiveData.value = true
        viewModelScope.launch {
            artistsRepository.getTopAlbums(artistMbid)
                .doOnEach { _progressLiveData.value = false }
                .fold({ error ->
                    triggerErrorSnackBar(error.errorMessage)
                }, { result ->
                    observeSavedAlbums(result)
                })
        }
    }

    private fun observeSavedAlbums(topAlbums: List<AlbumData>) {
        viewModelScope.launch {
            artistsRepository.observeSavedAlbums().collect { savedAlbumsResult ->
                val savedMbids = savedAlbumsResult.map { it.mbid }.toSet()
                _albumsLiveData.value = topAlbums.map { localAlbum ->
                    if (savedMbids.contains(localAlbum.mbid))
                        localAlbum.copy(isSaved = true)
                    else
                        localAlbum.copy(isSaved = false)
                }
            }
        }
    }

    // TODO: Move to use case?
    fun albumClicked(album: AlbumData) {
        viewModelScope.launch {
            if (album.mbid.isBlank()) {
                triggerErrorSnackBar(resources.getString(R.string.album_details_no_mbid))
            } else {
                _navigationAlbumFlow.send(
                    MbidNavigationData(
                        mbid = album.mbid,
                        name = album.name
                    )
                )
            }
        }
    }

    // TODO: Move to use case?
    fun albumFavoriteClicked(model: AlbumData) {
        viewModelScope.launch {
            when (model.isSaved) {
                true -> {
                    artistsRepository.deleteAlbum(model.mbid)
                }
                false -> {
                    artistsRepository.saveAlbum(model)
                }
            }
        }
    }
}