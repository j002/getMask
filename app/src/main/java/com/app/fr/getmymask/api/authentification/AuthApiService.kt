/*
 * Copyright (c) 2020 by Appndigital, Inc.
 * All Rights Reserved
 */

package com.app.fr.getmymask.api.authentification


import com.app.fr.getmymask.api.models.ResponseLogin
import io.reactivex.Single

interface AuthApiService {

    fun register(email: String, password: String): Single<Int>
    fun loginUser(email: String, password: String): Single<Boolean>

}
