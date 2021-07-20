package com.eazzyapps.test.data.models


import com.eazzyapps.test.domain.models.CommitInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommitInfoDto(
    val commit: Commit,
    val sha: String
)

data class Commit(
    val author: Author,
    val message: String,
)

data class Author(
    val date: String,
    val email: String,
    val name: String
)

fun CommitInfoDto.toDomain() = CommitInfo(
    sha = sha,
    date = commit.author.date,
    author = commit.author.name,
    message = commit.message
)

fun List<CommitInfoDto>.toDomain() = map { it.toDomain() }