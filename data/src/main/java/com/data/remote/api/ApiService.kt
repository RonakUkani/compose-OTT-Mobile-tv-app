package com.data.remote.api

import com.data.model.response.FilmsDTO
import com.data.model.response.UserDTO
import com.data.model.response.VideosListDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService(private var httpClient: HttpClient) {

    suspend fun fetchData(): FilmsDTO {
        return httpClient.get(ApiEndpoints.FILMS) {
            parameter(ApiParameters.FORMAT, ApiParametersValue.JSON)
        }.body()
    }

    suspend fun getUserInfo(): UserDTO {
        return httpClient.get(ApiEndpoints.PEOPLE) {
            parameter(ApiParameters.FORMAT, ApiParametersValue.JSON)
        }.body()
    }

    suspend fun getVideosList(key:String, page:String): VideosListDTO {
        return httpClient.get(ApiEndpoints.VIDEOS) {
            parameter(ApiParameters.KEY, key)
            parameter(ApiParameters.PAGE, page)
            parameter(ApiParameters.PER_PAGE, ApiParametersValue.PER_PAGE)
        }.body()
    }
}