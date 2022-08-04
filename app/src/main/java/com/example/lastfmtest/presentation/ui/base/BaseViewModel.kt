package com.example.lastfmtest.presentation.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    private val _errorSnackBarFlow = Channel<String>()
    val errorSnackBarFlow: Flow<String> = _errorSnackBarFlow.receiveAsFlow()
    protected val _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> = _progressLiveData
    private var _started: Boolean = false

    fun start() {
        if (_started) return
        started()
    }

    protected open fun started() {
        _started = true
    }

    protected fun triggerErrorSnackBar(errorMessage: String) {
        viewModelScope.launch {
            _errorSnackBarFlow.send(errorMessage)
        }
    }
}