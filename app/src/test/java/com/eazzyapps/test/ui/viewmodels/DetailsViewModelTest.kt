package com.eazzyapps.test.ui.viewmodels

import com.eazzyapps.test.ImmediateSchedulersRule
import com.eazzyapps.test.domain.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Observable.error
import io.reactivex.rxjava3.core.Observable.just
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val rxTestRule = ImmediateSchedulersRule()

    private val repository = mockk<Repository>()

    @Test
    fun successTest() {

        every { repository.getRepositoryById(fakeRepoId) } returns just(listOf(fakeRepository))
        every { repository.getRepositoryCommits(fakeRepository) } returns just(fakeCommits)

        val viewModel = spyk(DetailsViewModel(fakeRepoId, repository))

        viewModel.getData().test().assertValue(fakeCommits)

        verifyOrder {
            repository.getRepositoryById(fakeRepoId)
            viewModel.showLoading(true)
            viewModel.showDetails(any())
            repository.getRepositoryCommits(fakeRepository)
            viewModel.showLoading(false)
            viewModel.showCommits(any())
        }

    }

    @Test
    fun errorLoadingRepositoryDetailsTest() {

        val e = Exception("Repository not found")

        every { repository.getRepositoryById(fakeRepoId) } returns error(e)

        val viewModel = spyk(DetailsViewModel(fakeRepoId, repository))

        viewModel.getData().test().assertError(e)

        verifyOrder {
            repository.getRepositoryById(fakeRepoId)
            viewModel.showLoading(true)
            viewModel.showLoading(false)
            viewModel.showError(e)
        }

    }

    @Test
    fun errorLoadingRepositoryCommitsTest() {

        val e = Exception("Commits not found")

        every { repository.getRepositoryById(fakeRepoId) } returns just(listOf(fakeRepository))
        every { repository.getRepositoryCommits(fakeRepository) } returns error(e)

        val viewModel = spyk(DetailsViewModel(fakeRepoId, repository))

        viewModel.getData().test().assertError(e)

        verifyOrder {
            repository.getRepositoryById(fakeRepoId)
            viewModel.showLoading(true)
            viewModel.showDetails(any())
            repository.getRepositoryCommits(fakeRepository)
            viewModel.showLoading(false)
            viewModel.showError(e)
        }

    }

}