package com.domain.usecase

import com.data.result.NetworkResult
import com.domain.model.Films

interface GetHomePageUseCase {
    suspend operator fun invoke() : NetworkResult<Films>
}