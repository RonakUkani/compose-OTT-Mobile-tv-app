package com.data.di

import com.data.datasource.RemoteNetworkDataSource
import com.data.datasource.RemoteNetworkDataSourceImpl
import com.data.remote.api.ApiService
import com.data.remote.api.client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiService(httpClient)
    }

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        return client// or your specific configuration
    }

    @Provides
    @Singleton
    fun provideRemoteNetworkDataSource(
        apiService: ApiService
    ): RemoteNetworkDataSource {
        return RemoteNetworkDataSourceImpl(apiService)
    }
}