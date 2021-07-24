package com.ezzyapps.test.repositories.ui.details

import com.eazzyapps.test.ACCOUNT_OWNER
import com.ezzyapps.test.repositories.domain.models.CommitInfo
import com.ezzyapps.test.repositories.domain.models.GitHubRepo

val fakeRepoId = 1111

val fakeRepository = GitHubRepo(
    id = fakeRepoId,
    name = "Very popular repository",
    description = "So interesting description",
    owner = ACCOUNT_OWNER,
    avatarUrl = "",
    createdAt = "2018-10-18T00:00:00Z",
    license = "Free to use",
    watchers = 10,
    stars = 23,
    forks = 5
)

val fakeCommitInfo = CommitInfo(
    sha = "4ty56678678776",
    date = "2018-10-18T00:00:00Z",
    author = ACCOUNT_OWNER,
    message = "Initial commit"
)

val fakeCommits = listOf(
    fakeCommitInfo, fakeCommitInfo, fakeCommitInfo
)