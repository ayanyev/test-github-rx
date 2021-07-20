package com.eazzyapps.test.di

import com.eazzyapps.test.data.RemoteClient
import com.eazzyapps.test.data.RepoClient
import com.eazzyapps.test.data.RepositoryImpl
import com.eazzyapps.test.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun provideRemoteClient(): RepoClient {
            return RemoteClient.create(RepoClient::class.java)
        }
    }

    @Binds
    abstract fun bindRepository(
        repository: RepositoryImpl
    ): Repository

}