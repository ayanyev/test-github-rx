package com.eazzyapps.test.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.eazzyapps.test.ACCOUNT_OWNER
import com.eazzyapps.test.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repo: Repository) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _itemClicks = PublishSubject.create<Int>()

    val itemClicks: Observable<Int> = _itemClicks

    val publicRepos = ObservableField<List<RepoItemViewModel>>()

    val isLoading = ObservableBoolean(false)

    val errorMsg = ObservableField("")

    val errorMsgVisibility = ObservableField(false)

    init {

        disposables.add(
            repo.getPublicRepositories(ACCOUNT_OWNER)
                .doOnSubscribe { isLoading.set(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { hideError(); isLoading.set(false) }
                .doOnError { isLoading.set(false) }
                .subscribeBy(
                    onNext = { repos ->
                        publicRepos.set(
                            repos.map { repo ->
                                RepoItemViewModel(
                                    repo = repo,
                                    onClick = { r -> _itemClicks.onNext(r.id) }
                                )
                            }
                        )
                    },
                    onError = { e -> showError(e.message ?: "Smth happened!!!") }
                )
        )
    }

    private fun showError(msg: String) {
        errorMsg.set(msg)
        errorMsgVisibility.set(true)
    }

    private fun hideError() {
        errorMsgVisibility.set(false)
    }

    override fun onCleared() {
        disposables.dispose()
    }

}