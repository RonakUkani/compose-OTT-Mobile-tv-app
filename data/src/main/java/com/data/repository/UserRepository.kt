package com.data.repository

import com.data.model.response.UserDTO
import com.data.result.NetworkResult

interface UserRepository {
    suspend fun userInfo() : NetworkResult<UserDTO>
}