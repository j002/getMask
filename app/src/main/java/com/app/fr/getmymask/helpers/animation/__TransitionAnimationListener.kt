package com.app.fr.getmymask.helpers.animation

import androidx.transition.Transition


class __TransitionAnimationListener : Transition.TransitionListener {

    private var _onTransitionEnd: ((transition: Transition) -> Unit)? = null
    private var _onTransitionResume: ((transition: Transition) -> Unit)? = null
    private var _onTransitionPause: ((transition: Transition) -> Unit)? = null
    private var _onTransitionCancel: ((transition: Transition) -> Unit)? = null
    private var _onTransitionStart: ((transition: Transition) -> Unit)? = null

    override fun onTransitionEnd(transition: Transition) {
        _onTransitionEnd?.invoke(transition)
    }

    fun onTransitionEnd(func: (transition: Transition) -> Unit) {
        _onTransitionEnd = func
    }

    override fun onTransitionResume(transition: Transition) {
        _onTransitionResume?.invoke(transition)
    }

    fun onTransitionResume(func: (transition: Transition) -> Unit) {
        _onTransitionResume = func
    }

    override fun onTransitionPause(transition: Transition) {
        _onTransitionPause?.invoke(transition)
    }

    fun onTransitionPause(func: (transition: Transition) -> Unit) {
        _onTransitionPause = func
    }

    override fun onTransitionCancel(transition: Transition) {
        _onTransitionCancel?.invoke(transition)
    }

    fun onTransitionCancel(func: (transition: Transition) -> Unit) {
        _onTransitionCancel = func
    }

    override fun onTransitionStart(transition: Transition) {
        _onTransitionStart?.invoke(transition)
    }

    fun onTransitionStart(func: (transition: Transition) -> Unit) {
        _onTransitionStart = func
    }

}