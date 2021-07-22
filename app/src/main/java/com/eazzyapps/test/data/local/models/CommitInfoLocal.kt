package com.eazzyapps.test.data.local.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eazzyapps.test.domain.models.CommitInfo

@Entity(tableName = "commits")
data class CommitInfoLocal(
    @PrimaryKey val sha: String,
    val repoId: Int,
    val date: String,
    val email: String,
    val name: String,
    val message: String
)

fun CommitInfoLocal.toDomain() = CommitInfo(
    sha = sha,
    date = date,
    author = name,
    message = message
)

fun List<CommitInfoLocal>.toDomain() = map { it.toDomain() }