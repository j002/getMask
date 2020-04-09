
package com.app.fr.getmymask.api.helpers

import io.reactivex.Emitter
import io.reactivex.SingleEmitter

class EmitterErrorAdapter<T>(private val singleEmitter: SingleEmitter<T>) : Emitter<T> {
    override fun onComplete() {
        singleEmitter.onError(UnsupportedOperationException())
    }

    override fun onNext(value: T) {
        singleEmitter.onError(UnsupportedOperationException())
    }

    override fun onError(error: Throwable) {
        singleEmitter.onError(error)
    }
}