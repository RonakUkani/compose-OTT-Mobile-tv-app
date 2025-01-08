package com.data.repository

import com.data.model.response.FilmsDTO
import com.data.result.NetworkResult

interface HomeRepository {
    suspend fun getHomeList() : NetworkResult<FilmsDTO>
}