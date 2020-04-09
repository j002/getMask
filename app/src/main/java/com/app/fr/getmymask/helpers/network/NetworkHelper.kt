package com.app.fr.getmymask.helpers.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper {

    companion object {

        @JvmStatic
        fun isConnected(context: Context): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}