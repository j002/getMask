package com.leclub.app.helpers

import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.app.fr.getmymask.R

import com.app.fr.getmymask.core.BaseActivity

class FlashbarHelper {

    companion object {
        @JvmStatic
        fun createFlashBarError(activity: BaseActivity, title: String, message: String): Flashbar {
            return Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title(title)
                .message(message)
                .messageSizeInSp(12f)
                .primaryActionText(R.string.flashbar_error_retry)
                .primaryActionTapListener(object : Flashbar.OnActionTapListener {
                    override fun onActionTapped(bar: Flashbar) {
                        activity.finish()
                        activity.startActivity(activity.intent)
                    }
                })
                .backgroundColorRes(R.color.redErrorColorFlashBar)
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate()
                )
                .build()
        }

    }
}