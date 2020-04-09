/*
 * Copyright (c) 2020 by Appndigital, Inc.
 * All Rights Reserved
 */

package com.app.fr.getmymask.api.helpers

import io.reactivex.CompletableEmitter
import io.reactivex.Emitter

class CompletableEmitterErrorAdapter(private val completablEmitter: CompletableEmitter) : Emitter<Unit> {
    override fun onComplete() {
        completablEmitter.onError(UnsupportedOperationException())
    }

    override fun onNext(value: Unit) {
        completablEmitter.onError(UnsupportedOperationException())
    }

    override fun onError(error: Throwable) {
        completablEmitter.onError(error)
    }
}