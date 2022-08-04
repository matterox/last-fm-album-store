package com.example.lastfmtest.presentation.ui.home

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val resources: Resources
) : BaseViewModel() {

    private val _undoDeleteAlbumLiveData = MutableSharedFlow<AlbumData>()
    val undoDeleteAlbumLiveData: Flow<AlbumData> = _undoDeleteAlbumLiveData.asSharedFlow()
    private val _savedAlbumsLiveData = MutableLiveData<List<AlbumData>>()
    val savedAlbumsLiveData: LiveData<List<AlbumData>> = _savedAlbumsLiveData
    private val _navigationAlbumFlow = Channel<MbidNavigationData>()
    val navigationAlbumFlow: Flow<MbidNavigationData> = _navigationAlbumFlow.receiveAsFlow()

    override fun started() {
        super.started()
        viewModelScope.launch {
            artistsRepository.observeSavedAlbums().collect {
                _savedAlbumsLiveData.value = it.filter { it.isSaved }
            }
        }
    }

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

    fun onAlbumLikeClicked(model: AlbumData) {
        viewModelScope.launch {
            _undoDeleteAlbumLiveData.emit(model)
            artistsRepository.removeFromFavorites(model.mbid)
        }
    }

    fun addToFavorites(model: AlbumData) {
        viewModelScope.launch {
            artistsRepository.addToFavorites(model.mbid)
        }
    }

    fun removeAlbum(model: AlbumData) {
        viewModelScope.launch {
            artistsRepository.deleteAlbum(model.mbid)
        }
    }
}