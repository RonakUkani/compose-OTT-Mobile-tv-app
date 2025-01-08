package com.data.repository

import com.data.datasource.RemoteNetworkDataSource
import com.data.model.response.FilmsDTO
import com.data.result.NetworkResult
import com.data.result.toNetworkError
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteNetworkDataSource: RemoteNetworkDataSource) :
    HomeRepository {
    override suspend fun getHomeList(): NetworkResult<FilmsDTO> {
        return try {
            NetworkResult.Success(remoteNetworkDataSource.fetchData())
        } catch (e: Exception) {
            NetworkResult.Error(e.toNetworkError())
        }
    }
}