package com.data.result

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

fun Exception.toNetworkError(): String = when (this) {
    is ServerResponseException -> "Server error: ${this.response.status.description}"
    is ClientRequestException ->
        when (this.response.status.value) {
            400 -> "Bad request: ${this.response.status.description}"
            401 -> "Unauthorized: ${this.response.status.description}"
            403 -> "Forbidden: ${this.response.status.description}"
            404 -> "Not found: ${this.response.status.description}"
            else -> "Client error: ${this.response.status.description}"
        }

    is RedirectResponseException -> "Redirect error: ${this.response.status.description}"
    else -> "Unexpected error: ${this.localizedMessage}"
}