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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val resources: Resources
) : BaseViewModel() {

    private val _savedAlbumsLiveData = MutableLiveData<List<AlbumData>>()
    val savedAlbumsLiveData: LiveData<List<AlbumData>> = _savedAlbumsLiveData
    private val _navigationAlbumFlow = Channel<MbidNavigationData>()
    val navigationAlbumFlow: Flow<MbidNavigationData> = _navigationAlbumFlow.receiveAsFlow()

    override fun started() {
        super.started()
        viewModelScope.launch {
            artistsRepository.observeSavedAlbums().collect {
                _savedAlbumsLiveData.value = it
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