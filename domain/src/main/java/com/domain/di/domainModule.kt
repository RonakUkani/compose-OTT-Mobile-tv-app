package com.domain.di

import com.data.repository.HomeRepository
import com.data.repository.UserRepository
import com.data.repository.VideosRepository
import com.domain.mapper.FilmsMapper
import com.domain.mapper.UserMapper
import com.domain.mapper.VideosListMapper
import com.domain.usecase.GetHomePageUseCase
import com.domain.usecase.GetHomePageUseCaseImpl
import com.domain.usecase.GetVideoListUseCase
import com.domain.usecase.GetVideoListUseCaseImpl
import com.domain.usecase.UserInfoUseCase
import com.domain.usecase.UserInfoUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetHomePageUseCase(
        homeRepository: HomeRepository,
        filmsMapper: FilmsMapper
    ): GetHomePageUseCase {
        return GetHomePageUseCaseImpl(homeRepository, filmsMapper)
    }

    @Provides
    @Singleton
    fun provideFilmsMapper(): FilmsMapper {
        return FilmsMapper()
    }

    @Provides
    @Singleton
    fun provideUserInfoUseCase(
        userRepository: UserRepository,
        userMapper: UserMapper
    ): UserInfoUseCase {
        return UserInfoUseCaseImpl(userRepository, userMapper)
    }

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    @Singleton
    fun provideGetVideoListUseCase(
        videosRepository: VideosRepository,
        videoListMapper: VideosListMapper
    ): GetVideoListUseCase {
        return GetVideoListUseCaseImpl(videosRepository, videoListMapper)
    }

    @Provides
    @Singleton
    fun provideVideosListMapper(): VideosListMapper {
        return VideosListMapper()
    }
}