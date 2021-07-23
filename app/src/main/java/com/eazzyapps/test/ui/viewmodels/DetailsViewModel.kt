package com.eazzyapps.test.ui.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.customviews.commitshistory.CommitsHistoryViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class DetailsViewModel @AssistedInject constructor(

    @Assisted repoId: Int,
    repository: Repository

) : ViewModel() {

    private val disposables = CompositeDisposable()

    val details = ObservableField<Details>()

    val commits = ObservableField<CommitsHistoryViewModel>()

    val isLoading = ObservableBoolean(false)

    init {

        disposables.add(
            repository.getRepositoryById(repoId)
                .doOnSubscribe { isLoading.set(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.first() }
                .doOnNext { details.set(Details(it)) }
                .flatMap { repository.getRepositoryCommits(it) }
                .doOnNext { isLoading.set(false) }
                .subscribeBy(
                    onNext = { c -> commits.set(CommitsHistoryViewModel(c)) },
                    onError = { e -> Log.e(javaClass.simpleName, e.message ?: "smth happened") }
                )
        )

    }

    fun onResume() {
        commits.get()?.startRotate()
    }

    fun onPause() {
        commits.get()?.stopRotate()
    }

    override fun onCleared() {
        disposables.dispose()
    }

    class Details(repo: GitHubRepo) {

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