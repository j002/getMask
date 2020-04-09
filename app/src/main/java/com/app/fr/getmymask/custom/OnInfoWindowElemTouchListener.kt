package com.app.fr.getmymask.custom

import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

import com.google.android.gms.maps.model.Marker

abstract class OnInfoWindowElemTouchListener(private val view: View) : OnTouchListener {
    private val handler = Handler()

    private var marker: Marker? = null
    private var pressed = false

    private val confirmClickRunnable = Runnable {
        if (endPress()) {
            onClickConfirmed(view, marker)
        }
    }

    fun setMarker(marker: Marker) {
        this.marker = marker
    }

    override fun onTouch(vv: View, event: MotionEvent): Boolean {
        if (0 <= event.x && event.x <= view.width && 0 <= event.y && event.y <= view.height) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> startPress()

            // We need to delay releasing of the view a little so it shows the
            // pressed state on the screen
                MotionEvent.ACTION_UP -> handler.postDelayed(confirmClickRunnable, 150)

                MotionEvent.ACTION_CANCEL -> endPress()
                else -> {
                }
            }
        } else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress()
        }
        return false
    }

    private fun startPress() {
        if (!pressed) {
            pressed = true
            handler.removeCallbacks(confirmClickRunnable)
            if (marker != null)
                marker!!.showInfoWindow()
        }
    }

    private fun endPress(): Boolean {
        if (pressed) {
            this.pressed = false
            handler.removeCallbacks(confirmClickRunnable)
            if (marker != null)
                marker!!.showInfoWindow()
            return true
        } else
            return false
    }

    /**
     * This is called after a successful click
     */
    protected abstract fun onClickConfirmed(v: View, marker: Marker?)
}