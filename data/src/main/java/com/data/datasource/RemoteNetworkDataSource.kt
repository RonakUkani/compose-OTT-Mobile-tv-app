package com.data.datasource

import com.data.model.response.FilmsDTO
import com.data.model.response.UserDTO
import com.data.model.response.VideosListDTO

interface RemoteNetworkDataSource {
    suspend fun fetchData(): FilmsDTO
    suspend fun userInfo(): UserDTO
    suspend fun getVideosList(key:String, page:String): VideosListDTO
}