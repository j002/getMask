package com.app.fr.getmymask.ui.authentification


import com.app.fr.getmymask.api.authentification.AuthApiService
import com.app.fr.getmymask.core.BaseViewModel
import com.app.fr.getmymask.di.SingleLiveEvent
import com.app.fr.getmymask.token.TokenJWTService
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HomeAuthViewModel @Inject constructor(
    private val authApiService: AuthApiService, private val tokenJWTService: TokenJWTService
) :
    BaseViewModel() {

    private var responseLogin = SingleLiveEvent<Boolean>()
    private var responseRegister = SingleLiveEvent<Int>()


    fun getResponseLogin(): SingleLiveEvent<Boolean> {
        return responseLogin
    }

    fun getResponseRegister(): SingleLiveEvent<Int> {
        return responseRegister
    }

    fun clearResponseLogin() {
        responseLogin.value = null
    }

    fun loginUser(username: String, password: String) {
        authApiService.run {
            loginUser(username, password).subscribeBy(onSuccess = { response ->
                responseLogin.run {
                    postValue(response)
                   // saveTokensUser(response.token!!)
                }
            })
        }
    }

    fun registerUser(username: String, password: String) {
        authApiService.run {
            register(username, password).subscribeBy(onSuccess = { response ->
                responseRegister.run { postValue(response) }
            })
        }
    }



}