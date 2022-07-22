package com.example.lastfmtest.presentation.helper

import kotlinx.coroutines.flow.Flow

sealed class EitherResult<out L, out R> {
    data class Failure<out L>(val error: L) : EitherResult<L, Nothing>()
    data class Success<out R>(val data: R) : EitherResult<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isFailure get() = this is Failure<L>

    fun fold(failure: (L) -> Unit = {}, success: (R) -> Unit = {}): Unit =
        when (this) {
            is Failure -> failure(error)
            is Success -> success(data)
        }

    fun doOnEach(callback: () -> Unit): EitherResult<L, R> {
        callback.invoke()
        return this
    }
}

typealias EiterResultFlow<L, R> = Flow<EitherResult<L, R>>
