package com.eazzyapps.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eazzyapps.test.data.local.dao.CommitsDao
import com.eazzyapps.test.data.local.dao.ReposDao
import com.eazzyapps.test.data.local.models.CommitInfoLocal
import com.eazzyapps.test.data.local.models.GitHubRepoLocal

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