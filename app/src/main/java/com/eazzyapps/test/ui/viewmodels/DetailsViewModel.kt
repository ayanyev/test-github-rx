package com.eazzyapps.test.ui.viewmodels

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.customviews.commitshistory.CommitsHistoryViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class DetailsViewModel @AssistedInject constructor(

    @Assisted val repoId: Int,
    private val repository: Repository

) : ViewModel() {

    private val disposables = CompositeDisposable()

    val details = ObservableField<Details>()

    val commits = ObservableField<CommitsHistoryViewModel>()

    val isLoading = ObservableBoolean(false)

    @VisibleForTesting
    fun getData(): Observable<List<CommitInfo>> = repository.getRepositoryById(repoId)
        .doOnSubscribe { showLoading(true) }
        .observeOn(AndroidSchedulers.mainThread())
        .map { it.first() }
        .doOnNext { showDetails(Details(it)) }
        .flatMap { repository.getRepositoryCommits(it) }
        .doOnNext {
            showLoading(false)
            val commitsVm = CommitsHistoryViewModel(it)
            showCommits(commitsVm)
            commitsVm.startRotate()
        }
        .doOnError {
            showLoading(false)
            showError(it)
        }

    @VisibleForTesting
    fun showLoading(loading: Boolean) {
        isLoading.set(loading)
    }

    @VisibleForTesting
    fun showCommits(commitsVm: CommitsHistoryViewModel) {
        commits.set(commitsVm)
    }

    @VisibleForTesting
    fun showDetails(d: Details) {
        details.set(d)
    }

    fun showError(e: Throwable) {
        // show some meaningful message
    }

    fun onResume() {
        if (commits.get() == null) {
            disposables.add(
                getData().subscribeBy(
                    // leave it here to avoid onErrorNotImplemented exception
                    onError = { e -> Log.e(javaClass.simpleName, e.message ?: "smth happened") }
                )
            )
        } else {
            commits.get()?.startRotate()
        }
    }

    fun onPause() {
        commits.get()?.stopRotate()
    }

    override fun onCleared() {
        disposables.dispose()
    }

    data class Details(val repo: GitHubRepo) {

        val id: String = "${repo.id}"

        val name: String = repo.name

        val owner: String = repo.owner

        val desc: String = repo.description ?: "no description provided"

        val license: String = repo.license ?: "no license selected"

        val date: String = repo.createdAt ?: "no date"

    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(repoId: Int): DetailsViewModel
    }

    companion object {

        fun provideFactory(
            assistedFactory: AssistedFactory,
            repoId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(repoId) as T
            }
        }

    }

}