package com.example.lastfmtest.presentation.ui.search

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lastfmtest.R
import com.example.lastfmtest.domain.model.ArtistData
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
class SearchViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val resources: Resources
) : BaseViewModel() {

    private val _artistsLiveData = MutableLiveData<List<ArtistData>>()
    val artistsLiveData: LiveData<List<ArtistData>> = _artistsLiveData
    private val _searchQueryLiveData = MutableLiveData<String>()
    val searchQueryLiveData: LiveData<String> = _searchQueryLiveData
    private val _navigationArtistTopFlow = Channel<MbidNavigationData>()
    val navigationArtistTopFlow: Flow<MbidNavigationData> = _navigationArtistTopFlow.receiveAsFlow()


    fun searchPressed(input: String) {
        if (input.isBlank()) {
            triggerErrorSnackBar("Enter artist name")
            return
        }
        _searchQueryLiveData.value = input
        viewModelScope.launch {
            _progressLiveData.value = true
            artistsRepository.searchArtist(input)
                .doOnEach { _progressLiveData.value = false }
                .fold(
                    { error ->
                        triggerErrorSnackBar(error.errorMessage)
                    }, { result ->
                        _artistsLiveData.value = result
                    }
                )
        }
    }

    fun artistClicked(artistData: ArtistData) {
        viewModelScope.launch {
            if (artistData.mbid.isBlank()) {
                triggerErrorSnackBar(resources.getString(R.string.artist_playlist_no_mbid))
            } else {
                _navigationArtistTopFlow.send(
                    MbidNavigationData(
                        mbid = artistData.mbid,
                        name = artistData.name
                    )
                )
            }
        }
    }
}