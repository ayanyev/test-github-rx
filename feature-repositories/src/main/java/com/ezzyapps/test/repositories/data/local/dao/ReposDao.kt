package com.ezzyapps.test.repositories.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezzyapps.test.repositories.data.local.models.GitHubRepoLocal
import io.reactivex.rxjava3.core.Observable

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<GitHubRepoLocal>)

    @Query("SELECT * FROM repositories")
    fun loadAll(): Observable<List<GitHubRepoLocal>>

    @Query("SELECT * FROM repositories WHERE id = :id")
    fun findRepoById(id: Int): Observable<List<GitHubRepoLocal>>

}