package com.app.fr.getmymask.api.models

import androidx.annotation.Nullable
import com.squareup.moshi.Json

class ResponseLogin (
    @Nullable
    @field:Json(name = "token")
    val token: String? = null,
    @Nullable
    @field:Json(name = "error")
    val message: String? = null
)