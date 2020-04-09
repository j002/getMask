/*
 * Copyright (c) 2020by Appndigital, Inc.
 * All Rights Reserved
 */

package com.app.fr.getmymask.api.authentification

import com.app.fr.getmymask.api.models.ResponseLogin
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String
        ): Single<Response<Any>>


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("login")
    fun loginUser(@Field("email") email: String,
                  @Field("password") password: String
                  ): Observable<Response<ResponseLogin>>






}