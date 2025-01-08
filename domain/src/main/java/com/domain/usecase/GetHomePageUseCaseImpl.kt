package com.domain.usecase

import com.data.model.response.FilmsDTO
import com.data.repository.HomeRepository
import com.data.result.NetworkResult
import com.domain.mapper.FilmsMapper
import com.domain.model.Films
import javax.inject.Inject

class GetHomePageUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository,
    private val filmsMapper: FilmsMapper
) : GetHomePageUseCase {
    override suspend fun invoke(): NetworkResult<Films> {
        return homeRepository.getHomeList().handleNetWorkResult()
    }

    private fun NetworkResult<FilmsDTO>.handleNetWorkResult(): NetworkResult<Films> {
        return when (this) {
            is NetworkResult.Success -> {
                NetworkResult.Success(filmsMapper.map(data))
            }

            is NetworkResult.Error -> {
                NetworkResult.Error(message, cause)
            }
        }
    }
}