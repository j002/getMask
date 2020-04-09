package com.app.fr.getmymask.helpers.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class NetworkConnectionInterceptor : Interceptor {

    abstract fun isInternetAvailable(): Boolean

    abstract fun onInternetUnavailable()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isInternetAvailable()) {
            onInternetUnavailable()/*
            return NearPlaceResponse.Builder()
                .code(600)
                .protocol(Protocol.HTTP_2)
                .body(ResponseBody.create(MediaType.parse("id/plain"),"hello"))
                .message("Dummy response")
                .request(chain.request())
                .build()*/
        }
        return chain.proceed(request)
    }
}