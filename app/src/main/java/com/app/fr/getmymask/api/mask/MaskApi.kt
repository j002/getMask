package com.app.fr.getmymask.api.mask

import com.app.fr.getmymask.api.models.ResponseMasks
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface MaskApi {

    @GET("masks/around")
    fun getMasks(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("maxdistance") maxdistance: Int?
    ): Single<Response<List<ResponseMasks>>>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("masks")
    fun createMasks(
        @Field("latitude") latitude: Double?,
        @Field("longitude") longitude: Double?,
        @Field("quantity") quantity: Int?
    ): Single<Response<ResponseMasks>>
}