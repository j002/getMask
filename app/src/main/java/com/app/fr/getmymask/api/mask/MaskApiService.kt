package com.app.fr.getmymask.api.mask

import com.app.fr.getmymask.api.models.ResponseMasks
import io.reactivex.Observable
import io.reactivex.Single

interface MaskApiService {
    fun getMask(latitude: Double?, longitude: Double?): Observable<ResponseMasks>
    fun createMask(latitude: Double?, longitude: Double?,quantity:Int?): Single<ResponseMasks>

}