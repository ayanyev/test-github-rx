package com.eazzyapps.test.data.remote

import com.eazzyapps.test.data.remote.models.CommitInfoDto
import com.eazzyapps.test.data.remote.models.GitHubRepoDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RepoClient {

    @GET("users/{owner}/repos")
    fun getPublicRepositories(
        @Path("owner") owner: String
    ): Call<List<GitHubRepoDto>>

    @GET
    fun getRepositoryCommits(
        @Url url: String
    ): Call<List<CommitInfoDto>>
}