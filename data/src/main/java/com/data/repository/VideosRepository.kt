package com.data.repository

import com.data.model.response.VideosListDTO
import com.data.result.NetworkResult

interface VideosRepository {
    suspend fun getVideosList(key:String, page:String): NetworkResult<VideosListDTO>
}