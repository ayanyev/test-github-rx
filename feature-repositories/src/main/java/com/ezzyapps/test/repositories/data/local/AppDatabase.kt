package com.ezzyapps.test.repositories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezzyapps.test.repositories.data.local.dao.CommitsDao
import com.ezzyapps.test.repositories.data.local.dao.ReposDao
import com.ezzyapps.test.repositories.data.local.models.CommitInfoLocal
import com.ezzyapps.test.repositories.data.local.models.GitHubRepoLocal

@Database(
    version = 1,
    entities = [
        GitHubRepoLocal::class,
        CommitInfoLocal::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reposDao(): ReposDao
    abstract fun commitsDao(): CommitsDao
}