package com.app.fr.getmymask.core

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposComposite()
    }

    protected fun disposComposite() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}