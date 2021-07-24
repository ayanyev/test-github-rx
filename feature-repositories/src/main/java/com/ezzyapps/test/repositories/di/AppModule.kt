package com.ezzyapps.test.repositories.di

import android.content.Context
import androidx.room.Room
import com.ezzyapps.test.repositories.data.remote.RemoteClient
import com.ezzyapps.test.repositories.data.remote.RepoClient
import com.ezzyapps.test.repositories.data.RepositoryImpl
import com.ezzyapps.test.repositories.data.local.AppDatabase
import com.ezzyapps.test.repositories.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {

        @Provides
        @Singleton
        fun provideRemoteClient(): RepoClient =
            RemoteClient.create(RepoClient::class.java)

        @Provides
        @Singleton
        fun provideLocalDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "appDB")
                .build()
                .apply { Thread { clearAllTables() }.start() } // clear db at startup
    }

    @Binds
    abstract fun bindRepository(
        repository: RepositoryImpl
    ): Repository

}