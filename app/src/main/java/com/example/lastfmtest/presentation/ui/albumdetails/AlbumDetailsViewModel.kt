package com.example.lastfmtest.presentation.ui.albumdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lastfmtest.domain.model.AlbumDetailsData
import com.example.lastfmtest.domain.repository.ArtistsRepository
import com.example.lastfmtest.presentation.helper.EitherResult
import com.example.lastfmtest.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository
): BaseViewModel() {
    lateinit var albumMbid: String
    lateinit var albumName: String

    private val _albumNameLiveData = MutableLiveData<String>()
    val albumNameLiveData: LiveData<String> = _albumNameLiveData
    private val _albumDetailsState = MutableStateFlow(AlbumDetailsData.EMPTY)
    val albumsLiveData: StateFlow<AlbumDetailsData> = _albumDetailsState.asStateFlow()

    override fun start() {
        super.start()
        _albumNameLiveData.value = albumName
        viewModelScope.launch {
            artistsRepository.observeAlbumDetails(albumMbid).collect { result ->
                when (result) {
                    is EitherResult.Success -> {
                        _albumDetailsState.emit(result.data)
                    }
                    is EitherResult.Failure -> {
                        triggerErrorSnackBar(result.error.errorMessage)
                    }
                }
            }
        }
    }
}