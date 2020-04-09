package com.app.fr.getmymask.helpers.animation

import androidx.transition.Transition


inline fun Transition.addListener(
    func: __TransitionAnimationListener.() -> Unit) {
    val listener = __TransitionAnimationListener()
    listener.func()
    addListener(listener)
}