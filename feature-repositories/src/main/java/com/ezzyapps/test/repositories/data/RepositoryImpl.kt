package com.ezzyapps.test.repositories.data

import com.ezzyapps.test.repositories.data.local.AppDatabase
import com.ezzyapps.test.repositories.data.local.models.toDomain
import com.ezzyapps.test.repositories.data.remote.RepoClient
import com.ezzyapps.test.repositories.data.remote.models.CommitInfoDto
import com.ezzyapps.test.repositories.data.remote.models.toLocal
import com.ezzyapps.test.repositories.domain.Repository
import com.ezzyapps.test.repositories.domain.models.CommitInfo
import com.ezzyapps.test.repositories.domain.models.GitHubRepo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RepositoryImpl @Inject constructor(

    private val remoteClient: RepoClient,
    private val db: AppDatabase

) : Repository {

    override fun getRepositoryById(id: Int): Observable<List<GitHubRepo>> {
        return db.reposDao().findRepoById(id)
            .subscribeOn(Schedulers.io())
            .map { it.toDomain() }
    }

    // no pagination implemented
    override fun getPublicRepositories(owner: String): Observable<List<GitHubRepo>> {
        return db.reposDao().loadAll()
            .subscribeOn(Schedulers.io())
            .doOnNext { list ->
                if (list.isEmpty()) fetchRemoteRepositories(owner)
            }
            .filter { it.isNotEmpty() }
            .map { it.toDomain() }
    }

    // get all commits at once
    // for pagination used next page link from response headers
    override fun getRepositoryCommits(repo: GitHubRepo): Observable<List<CommitInfo>> {
        return db.commitsDao().loadCommitsForRepo(repo.id)
            .subscribeOn(Schedulers.io())
            .doOnNext { list ->
                if (list.isEmpty()) fetchRemoteRepositoryCommits(repo)
            }
            .filter { it.isNotEmpty() }
            .map { it.toDomain() }
    }

    private fun fetchRemoteRepositories(owner: String) {
        val response = remoteClient.getPublicRepositories(owner).execute()
        if (response.isSuccessful) {
            if (response.body() != null && response.body()!!.isNotEmpty()) {
                db.reposDao().insertAll(response.body()!!.toLocal())
            } else throw Exception("Received no repositories")
        } else throw Exception("Error while fetching repositories (code: ${response.code()})")
    }

    private fun fetchRemoteRepositoryCommits(repo: GitHubRepo) {
        val commits = mutableListOf<CommitInfoDto>()
        var nextLink: String? = "https://api.github.com/repos/${repo.owner}/${repo.name}/commits?per_page=100&page=1"
        while (nextLink != null) {
            val response = remoteClient.getRepositoryCommits(nextLink).execute()
            if (response.isSuccessful) {
                nextLink = response.headers()["link"]?.parseLinks()?.get("next")
                commits.addAll(response.body() ?: listOf())
            } else throw Exception("Error while fetching commits (code: ${response.code()})")
        }
        db.commitsDao().insertAll(commits.toLocal(repo.id))
    }

    // link header in response
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=9>; rel="next",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=9>; rel="last",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=1>; rel="first",
    // <https://api.github.com/repositories/10791045/commits?per_page=10&page=7>; rel="prev"

    private fun String.parseLinks(): Map<String?, String?> =
        split(",").associateBy(
            keySelector = { Regex("(?<=rel=\")\\D{4}").find(it)?.value },
            valueTransform = { Regex("[^<]+(?=>;)").find(it)?.value }
        )

}