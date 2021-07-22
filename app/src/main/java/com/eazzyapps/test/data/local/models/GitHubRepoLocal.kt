package com.eazzyapps.test.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eazzyapps.test.domain.models.GitHubRepo

@Entity(tableName = "repositories")
data class GitHubRepoLocal(
    @PrimaryKey val id: Int,
    val description: String?,
    val license: String?,
    val createdAt: String?,
    val authorName: String,
    val ownerLogin: String,
    val authorAvatarUrl: String,
    val forksCount: Int,
    val stargazersCount: Int,
    val watchersCount: Int
)

fun GitHubRepoLocal.toDomain() = GitHubRepo(
    id = id,
    name = authorName,
    description = description,
    owner = ownerLogin,
    avatarUrl = authorAvatarUrl,
    createdAt = createdAt,
    license = license,
    forks = forksCount,
    stars = stargazersCount,
    watchers = watchersCount
)

fun List<GitHubRepoLocal>.toDomain() = map { it.toDomain() }