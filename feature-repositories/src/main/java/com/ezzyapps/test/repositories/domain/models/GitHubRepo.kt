package com.ezzyapps.test.repositories.domain.models

data class GitHubRepo(
    val id: Int,
    val name: String,
    val description: String?,
    val owner: String,
    val avatarUrl: String,
    val createdAt: String?,
    val license: String?,
    val forks: Int,
    val stars: Int,
    val watchers: Int
)