package com.eazzyapps.test.data.models

import com.eazzyapps.test.domain.models.GitHubRepo

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

data class Owner(
    val login: String,
    val avatar_url: String
)

data class License(
    val name: String
)

fun GitHubRepoDto.toDomain() = GitHubRepo(
    id = id,
    name = name,
    description = description,
    owner = owner.login,
    avatarUrl = owner.avatar_url,
    createdAt = createdAt,
    license = license?.name,
    forks = forks_count,
    stars = stargazers_count,
    watchers = watchers_count
)

fun List<GitHubRepoDto>.toDomain() = map { it.toDomain() }