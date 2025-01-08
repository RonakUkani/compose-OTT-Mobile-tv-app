package com.data.di

import com.data.datasource.RemoteNetworkDataSource
import com.data.repository.HomeRepository
import com.data.repository.HomeRepositoryImpl
import com.data.repository.UserRepository
import com.data.repository.UserRepositoryImpl
import com.data.repository.VideosRepository
import com.data.repository.VideosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepository(
        remoteNetworkDataSource: RemoteNetworkDataSource
    ): HomeRepository {
        return HomeRepositoryImpl(remoteNetworkDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteNetworkDataSource: RemoteNetworkDataSource
    ): UserRepository {
        return UserRepositoryImpl(remoteNetworkDataSource)
    }

    @Provides
    @Singleton
    fun provideVideosRepository(
        remoteNetworkDataSource: RemoteNetworkDataSource
    ): VideosRepository {
        return VideosRepositoryImpl(remoteNetworkDataSource)
    }
}