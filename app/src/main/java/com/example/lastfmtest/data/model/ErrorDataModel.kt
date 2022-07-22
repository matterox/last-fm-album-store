package com.example.lastfmtest.data.model

import com.example.lastfmtest.domain.model.DefaultDomainError
import com.squareup.moshi.Json

open class ErrorDataModel(
    @Json(name = "message") val message: String? = null,
    @Json(name = "error") val error: Int? = null
)

fun ErrorDataModel?.toDomainModel(networkError: String): DefaultDomainError =
    DefaultDomainError(errorMessage = this?.let { "Error: ${message ?: "unknown"}.${error?.let { " Code: $it" }}" } ?: networkError)