package com.example.lastfmtest.data.retrofit

import com.example.lastfmtest.data.model.ErrorDataModel
import com.example.lastfmtest.data.model.toDomainModel
import com.example.lastfmtest.domain.model.DefaultDomainError
import com.example.lastfmtest.presentation.helper.EitherResult
import com.squareup.moshi.Moshi
import retrofit2.Response
import java.io.InterruptedIOException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

class RequestHandler(val moshi: Moshi) {
    val unknownError = "Unknown error."
    private val networkError = "Network error. Check your connection."

    inline fun <reified SuccessDataModel, reified SuccessDomainModel> safeRequest(
        response: () -> Response<SuccessDataModel>,
        successTransform: (SuccessDataModel) -> SuccessDomainModel
    ): EitherResult<DefaultDomainError, SuccessDomainModel> {
        return safeRequest(
            response = response,
            successTransform = successTransform,
            failureTransform = { failureDataModel: ErrorDataModel?, networkError: String? ->
                failureDataModel.toDomainModel(
                    networkError ?: unknownError
                )
            }
        )
    }


    inline fun <reified SuccessDataModel, reified SuccessDomainModel, reified FailureDataModel : ErrorDataModel, FailureDomainModel> safeRequest(
        response: () -> Response<SuccessDataModel>,
        successTransform: (SuccessDataModel) -> SuccessDomainModel,
        failureTransform: (FailureDataModel?, String?) -> FailureDomainModel
    ): EitherResult<FailureDomainModel, SuccessDomainModel> {
        try {
            val result = response.invoke()
            when (result.isSuccessful) {
                true -> return EitherResult.Success(
                    successTransform(
                        (result.body() ?: Any() as SuccessDataModel)
                    )
                )
                false -> {
                    val error = runCatching { result.errorBody()?.string() }.getOrNull()
                    error ?: return EitherResult.Failure(failureTransform(null, unknownError))
                    val jsonAdapter = moshi.adapter(FailureDataModel::class.java)
                    val responseError = runCatching { jsonAdapter.fromJson(error) }.getOrNull()
                    return EitherResult.Failure(
                        failureTransform(
                            responseError,
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            return when (e) {
                is ConnectException,
                is SocketException,
                is UnknownHostException,
                is InterruptedIOException -> {
                    EitherResult.Failure(failureTransform(null, createNetworkErrorMessage(e)))
                }
                else -> {
                    EitherResult.Failure(failureTransform(null, e.message ?: unknownError))
                }
            }
        }
    }

    fun createNetworkErrorMessage(e: Throwable): String {
        return e.message?.run {
            "${networkError}\n$this"
        } ?: networkError
    }
}