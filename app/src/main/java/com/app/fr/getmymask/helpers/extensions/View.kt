package com.app.fr.getmymask.helpers.extensions

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.app.fr.getmymask.R

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.slideUp(context: Context) {
    this.show()
    val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
    this.startAnimation(animation)
}

fun View.slideDown(context: Context) {
    this.gone()
    val animation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
    this.startAnimation(animation)
}
