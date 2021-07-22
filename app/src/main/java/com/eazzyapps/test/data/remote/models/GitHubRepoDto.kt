package com.eazzyapps.test.data.remote.models

import com.eazzyapps.test.data.local.models.GitHubRepoLocal
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubRepoDto(
    val id: Int,
    val name: String,
    val description: String?,
    val owner: Owner,
    val createdAt: String?,
    val license: License?,
    val forks_count: Int,
    val stargazers_count: Int,
    val watchers_count: Int
)

@JsonClass(generateAdapter = true)
data class Owner(
    val login: String,
    val avatar_url: String
)

@JsonClass(generateAdapter = true)
data class License(
    val name: String
)

fun GitHubRepoDto.toLocal() = GitHubRepoLocal(
    id = id,
    authorName = name,
    description = description,
    ownerLogin = owner.login,
    authorAvatarUrl = owner.avatar_url,
    createdAt = createdAt,
    license = license?.name,
    forksCount = forks_count,
    stargazersCount = stargazers_count,
    watchersCount = watchers_count
)

fun List<GitHubRepoDto>.toLocal() = map { it.toLocal() }