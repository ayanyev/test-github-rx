package com.eazzyapps.test.domain

import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun getPublicRepositories(owner: String): Observable<List<GitHubRepo>>

    fun getRepositoryById(id: Int): Observable<List<GitHubRepo>>

    fun getRepositoryCommits(repo: GitHubRepo): Observable<List<CommitInfo>>

}