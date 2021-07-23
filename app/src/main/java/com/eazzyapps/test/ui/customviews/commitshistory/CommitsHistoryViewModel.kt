package com.eazzyapps.test.ui.customviews.commitshistory

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.eazzyapps.test.domain.models.CommitInfo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CommitsHistoryViewModel(

    commits: List<CommitInfo>,
    private val updateFrequencyInMillis: Long = 1_500

) : ViewModel() {

    val monthOne = ObservableField<MonthViewModel>()

    val monthTwo = ObservableField<MonthViewModel>()

    val monthThee = ObservableField<MonthViewModel>()

    private val commitsCountMap = commits
        .groupBy(
            keySelector = { it.date.parseToMonthYear() },
            valueTransform = { 1 }
        )
        .mapValues { (_, v) -> v.sum() }

    private val maxMonthlyCount = commitsCountMap.values.maxOrNull() ?: 0

    private val monthlyViewModels = commitsCountMap
        .map { (k, v) -> MonthViewModel(maxMonthlyCount, v, k) }
        .chunked(3)

    init { startRotate() }

    private var disposable: Disposable? = null

    private var currentChunk = 0

    fun startRotate() {
        val chunksCount = monthlyViewModels.size
        disposable = Observable.interval(0, updateFrequencyInMillis, TimeUnit.MILLISECONDS)
            .subscribeBy(
                onNext = {
                    monthOne.set(monthlyViewModels[currentChunk][0])
                    monthTwo.set(monthlyViewModels.getOrNull(currentChunk)?.getOrNull(1))
                    monthThee.set(monthlyViewModels.getOrNull(currentChunk)?.getOrNull(2))
                    if (currentChunk == chunksCount - 1) currentChunk = 0
                    else currentChunk++
                },
                onError = {
                    Log.e(javaClass.simpleName, it.message ?: "No message", it)
                }
            )
    }

    fun stopRotate() {
        disposable?.dispose()
    }

    private fun String.parseToMonthYear(): String {
        val fromFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val toFormat = "MMMyy"
        return SimpleDateFormat(fromFormat, Locale.getDefault()).parse(this)?.let { date ->
            SimpleDateFormat(toFormat, Locale.getDefault()).format(date)
        } ?: throw IllegalArgumentException("Commit date not parsed: $this")
    }

}