package com.data.repository

import com.data.datasource.RemoteNetworkDataSource
import com.data.model.response.UserDTO
import com.data.result.NetworkResult
import com.data.result.toNetworkError
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val remoteNetworkDataSource: RemoteNetworkDataSource) :
    UserRepository {
    override suspend fun userInfo(): NetworkResult<UserDTO> {
        return try {
            NetworkResult.Success(remoteNetworkDataSource.userInfo())
        } catch (e: Exception) {
            NetworkResult.Error(e.toNetworkError())
        }
    }

}