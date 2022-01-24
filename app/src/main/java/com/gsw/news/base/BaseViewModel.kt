package com.gsw.news.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.gsw.news.other.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private var compositeDisposable: CompositeDisposable? = null
    val loading: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }
    val loading2: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }
    val error: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val error2: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun launch(job: () -> Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(job())
    }

    @CallSuper
    override fun onCleared() {
        compositeDisposable?.apply {
            dispose()
            clear()
            compositeDisposable = null
        }
        super.onCleared()
    }
}