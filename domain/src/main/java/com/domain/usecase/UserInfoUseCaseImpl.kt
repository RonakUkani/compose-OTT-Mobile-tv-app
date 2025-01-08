package com.domain.usecase

import com.data.model.response.UserDTO
import com.data.repository.UserRepository
import com.data.result.NetworkResult
import com.domain.mapper.UserMapper
import com.domain.model.User
import javax.inject.Inject

class UserInfoUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserInfoUseCase {
    override suspend fun invoke(): NetworkResult<User> {
        return userRepository.userInfo().handleNetWorkResult()
    }

    private fun NetworkResult<UserDTO>.handleNetWorkResult(): NetworkResult<User> {
        return when (this) {
            is NetworkResult.Success -> {
                NetworkResult.Success(userMapper.map(data))
            }

            is NetworkResult.Error -> {
                NetworkResult.Error(message, cause)
            }
        }
    }
}