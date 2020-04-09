package com.app.fr.getmymask.helpers.extensions

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair


inline fun <reified T : Any> AppCompatActivity.startActivityWithBundle(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : Any> AppCompatActivity.startActivityForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent, requestCode)
}


inline fun <reified T : Any> AppCompatActivity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    finish()
}

inline fun <reified T : Any> AppCompatActivity.startActivityWithoutFinish() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Any> AppCompatActivity.startActivityWithAnimation(vararg viewToAnimate: View) {
    val intent = Intent(this, T::class.java)
    val options = if (viewToAnimate.size == 1) {
        ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            viewToAnimate[0],
            viewToAnimate[0].transitionName
        )
    } else {
        val pairViews = ArrayList<Pair<View, String>>()
        viewToAnimate.forEach {
            pairViews.add(Pair.create(it, it.transitionName))
        }
        val pairArray: Array<Pair<View, String>> = pairViews.toTypedArray()

        ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairArray)
    }

    ActivityCompat.startActivity(this, intent, options.toBundle())
}