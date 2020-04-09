
package com.app.fr.getmymask.helpers.extensions

import okhttp3.MediaType
import okhttp3.RequestBody

fun String.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("id/plain"), this)
}

fun Int.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("id/plain"), this.toString())
}

fun Double.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("id/plain"), this.toString())
}