package com.ezzyapps.test.repositories.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezzyapps.test.repositories.data.local.models.CommitInfoLocal
import io.reactivex.rxjava3.core.Observable

@Dao
interface CommitsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(commits: List<CommitInfoLocal>)

    @Query("SELECT * FROM commits WHERE repoId = :repoId")
    fun loadCommitsForRepo(repoId: Int): Observable<List<CommitInfoLocal>>

}