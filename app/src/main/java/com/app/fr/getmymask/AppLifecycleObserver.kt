package com.app.fr.getmymask

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AppLifecycleObserver(app: App) : LifecycleObserver {

    var appLifecycleListener: AppLifecycleListener = app

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
       appLifecycleListener.onEnteringForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        appLifecycleListener.onEnteringBackGround()
    }

    interface AppLifecycleListener {
        fun onEnteringForeground()
        fun onEnteringBackGround()
    }
}