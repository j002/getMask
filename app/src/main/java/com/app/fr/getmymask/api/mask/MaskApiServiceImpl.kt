package com.app.fr.getmymask.api.mask

import android.content.Context
import android.util.Log
import com.app.fr.getmymask.Constants.Companion.DISTANCE_MAX
import com.app.fr.getmymask.api.ApiClientImpl
import com.app.fr.getmymask.api.helpers.EmitterErrorAdapter
import com.app.fr.getmymask.api.models.ResponseMasks
import com.app.fr.getmymask.di.GlobalInjectorModule_ProvideApplicationFactory.provideApplication
import com.crashlytics.android.Crashlytics
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import retrofit2.Retrofit
import javax.inject.Inject

class MaskApiServiceImpl @Inject constructor(val context: Context, retrofit: Retrofit) :
    MaskApiService, ApiClientImpl() {

    private var maskApi: MaskApi = retrofit.create(MaskApi::class.java)

    override fun getMask(latitude: Double?, longitude: Double?): Observable<ResponseMasks> =
        Observable.create { emitter ->

            doAsync {
                maskApi.getMasks(latitude, longitude, DISTANCE_MAX)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { response ->
                            if (response.isSuccessful) {
                                response.body()!!.forEach { mask ->
                                    emitter.onNext(mask)
                                }
                                emitter.onComplete()

                            } else {
                                handleStatusCodeToSendEmitter(emitter, response.code())
                            }
                        },
                        onError = {
                            Crashlytics.log(it.localizedMessage)
                            Logger.e(it.localizedMessage)
                            emitter.onError(it)
                        }

                    )
            }
        }

    override fun createMask(latitude: Double?, longitude: Double?, quantity: Int?): Single<ResponseMasks> = Single.create { emitter ->
        maskApi.run {
            createMasks(
                latitude,
                longitude,
                quantity
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { response ->
                        if (response.isSuccessful) {
                            emitter.onSuccess(response.body()!!)
                        Log.d("MASKCREATE",response.toString())
                       } else {
                            handleStatusCodeToSendEmitter(
                                EmitterErrorAdapter(emitter),
                                response.code()
                            )
                        }
                    },
                    onError = { error ->
                        Crashlytics.log(error.localizedMessage)
                        Logger.e(error.localizedMessage)
                        emitter.onError(error)
                    }
                )
        }
    }
}