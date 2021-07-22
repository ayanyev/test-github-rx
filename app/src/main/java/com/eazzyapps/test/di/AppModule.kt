package com.eazzyapps.test.di

import android.content.Context
import androidx.room.Room
import com.eazzyapps.test.data.remote.RemoteClient
import com.eazzyapps.test.data.remote.RepoClient
import com.eazzyapps.test.data.RepositoryImpl
import com.eazzyapps.test.data.local.AppDatabase
import com.eazzyapps.test.domain.Repository
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