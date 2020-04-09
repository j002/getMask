package com.app.fr.getmymask

class Constants {

    companion object {
        const val DEV_URL = "https://mtw-dev.herokuapp.com/"
        const val PROD_URL = "https://mtw-dev.herokuapp.com/"
        const val USER_PREFERENCES_KEY = "USER_PREFERENCES_KEY"
        const val USER_TOKEN_PREF_KEY = "USER_TOKEN_PREF_KEY"
        const val USER_REFRESH_TOKEN_PREF_KEY = "USER_REFRESH_TOKEN_PREF_KEY"
        const val FCM_TOKEN_PREFERENCES_KEY = "FCM_TOKEN_PREFERENCES_KEY"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val DISTANCE_MAX=10000000
        val PREFERENCE_KEY = "userKey"
        val TOKEN_KEY = "tokenKey"
        val EMAIL_KEY = "emailKey"
        val PASSWORD_KEY = "passwordKey"




    }

    enum class HTTPStatus constructor(val code: Int) {
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        BAD_EMAIL(422),
        NOT_FOUND(404),
        INTERNAL_SERVER_ERROR(500);

        companion object {
            fun from(findValue: Int): HTTPStatus =
                HTTPStatus.values().first { it.code == findValue }
        }
    }


}