package com.ezzyapps.test.repositories.domain

import com.ezzyapps.test.repositories.domain.models.CommitInfo
import com.ezzyapps.test.repositories.domain.models.GitHubRepo
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun getPublicRepositories(owner: String): Observable<List<GitHubRepo>>

    fun getRepositoryById(id: Int): Observable<List<GitHubRepo>>

    fun getRepositoryCommits(repo: GitHubRepo): Observable<List<CommitInfo>>

}