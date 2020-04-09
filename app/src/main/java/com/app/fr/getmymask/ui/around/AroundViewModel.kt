package com.app.fr.getmymask.ui.around

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.fr.getmymask.api.mask.MaskApiServiceImpl
import com.app.fr.getmymask.api.models.ResponseMasks
import com.app.fr.getmymask.core.BaseViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AroundViewModel @Inject constructor(private val maskApiService: MaskApiServiceImpl) :
    BaseViewModel() {

    private var masks = MutableLiveData<List<ResponseMasks>>()
    private var mask = MutableLiveData<ResponseMasks>()

    fun onMasksFound(): LiveData<List<ResponseMasks>> {
        return masks
    }

    fun onMasksCreate(): LiveData<ResponseMasks>{
        return mask
    }

    fun getMasksByDistance(latitude: Double?, longitude: Double?) {
        val listMask: MutableList<ResponseMasks> = mutableListOf()

        maskApiService.run {
            getMask(latitude, longitude).subscribeBy(
                onNext = { responseMask ->
                   listMask.add(responseMask)
                },
                onComplete = {
                    masks.postValue(listMask)
                }
            )
        }

    }

    fun createMask(latitude: Double?, longitude: Double?,quantity:Int?) {
        maskApiService.run {
            createMask(latitude, longitude,quantity).subscribeBy(
                onSuccess = { responseMask ->
                    mask.postValue(responseMask)
                }
            )
        }

    }

}