package com.eazzyapps.test.data

import com.eazzyapps.test.data.models.CommitInfoDto
import com.eazzyapps.test.data.models.GitHubRepoDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RepoClient {

    @GET("users/{owner}/repos")
    fun getPublicRepositories(
        @Path("owner") owner: String
    ): Observable<List<GitHubRepoDto>>

    @GET
    fun getRepositoryCommits(@Url url: String): Call<List<CommitInfoDto>>
}