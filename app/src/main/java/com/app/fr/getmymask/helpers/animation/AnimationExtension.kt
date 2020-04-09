package com.app.fr.getmymask.helpers.animation

import android.view.animation.Animation

inline fun Animation.setAnimationListener(
    func: __AnimationListener.() -> Unit) {
    val listener = __AnimationListener()
    listener.func()
    setAnimationListener(listener)
}