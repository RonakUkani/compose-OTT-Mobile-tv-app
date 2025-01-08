package com.data.datasource

import com.data.model.response.FilmsDTO
import com.data.model.response.UserDTO
import com.data.model.response.VideosListDTO
import com.data.remote.api.ApiService
import javax.inject.Inject

class RemoteNetworkDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    RemoteNetworkDataSource {
    override suspend fun fetchData(): FilmsDTO {
        return apiService.fetchData()
    }

    override suspend fun userInfo(): UserDTO {
        return apiService.getUserInfo()
    }

    override suspend fun getVideosList(key:String, page:String): VideosListDTO {
        return apiService.getVideosList(key, page)
    }
}