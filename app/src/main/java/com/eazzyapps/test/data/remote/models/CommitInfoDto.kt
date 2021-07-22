package com.eazzyapps.test.data.remote.models


import com.eazzyapps.test.data.local.models.CommitInfoLocal
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

fun CommitInfoDto.toLocal(repoId: Int) = CommitInfoLocal(
    sha = sha,
    repoId = repoId,
    date = commit.author.date,
    name = commit.author.name,
    message = commit.message,
    email = commit.author.email
)

fun List<CommitInfoDto>.toLocal(repoId: Int) = map { it.toLocal(repoId) }