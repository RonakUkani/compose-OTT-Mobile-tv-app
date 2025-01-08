package com.data.repository

import com.data.datasource.RemoteNetworkDataSource
import com.data.model.response.VideosListDTO
import com.data.result.NetworkResult
import com.data.result.toNetworkError
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(private val remoteNetworkDataSource: RemoteNetworkDataSource) :
    VideosRepository {
    override suspend fun getVideosList(key: String, page: String): NetworkResult<VideosListDTO> {
        return try {
            NetworkResult.Success(remoteNetworkDataSource.getVideosList(key, page))
        } catch (e: Exception) {
            NetworkResult.Error(e.toNetworkError())
        }
    }
}