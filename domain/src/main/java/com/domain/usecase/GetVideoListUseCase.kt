package com.domain.usecase

import com.data.result.NetworkResult
import com.domain.model.VideosList

interface GetVideoListUseCase {
    suspend operator fun invoke(key:String, page:String) : NetworkResult<VideosList>
}