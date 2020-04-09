
package com.app.fr.getmymask.token

import io.reactivex.Completable

interface TokenJWTService {

    fun saveTokenIfValid(token: String): Completable

    fun saveRefreshToken(refreshToken: String): Completable

    fun getToken(): String

    fun getRefreshToken(): String

    fun hasTokenValid(): Boolean

    fun deleteToken()
}