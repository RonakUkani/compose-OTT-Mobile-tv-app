package com.domain.usecase

import com.data.result.NetworkResult
import com.domain.model.User

interface UserInfoUseCase {
    suspend operator fun invoke() : NetworkResult<User>
}