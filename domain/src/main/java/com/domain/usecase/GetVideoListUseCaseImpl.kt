package com.domain.usecase

import com.data.repository.VideosRepository
import com.data.result.NetworkResult
import com.domain.mapper.VideosListMapper
import com.domain.model.VideosList
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val videosRepository: VideosRepository,
    private val videoListMapper: VideosListMapper
) : GetVideoListUseCase {
    override suspend fun invoke(key: String, page: String): NetworkResult<VideosList> {
        return videosRepository.getVideosList(key, page).handleNetworkResult {
            videoListMapper.map(it)
        }
    }

    private inline fun <Input, Output> NetworkResult<Input>.handleNetworkResult(
        transform: (Input) -> Output
    ): NetworkResult<Output> {
        return when (this) {
            is NetworkResult.Success -> {
                NetworkResult.Success(transform(data))
            }

            is NetworkResult.Error -> {
                NetworkResult.Error(message, cause)
            }
        }
    }
}