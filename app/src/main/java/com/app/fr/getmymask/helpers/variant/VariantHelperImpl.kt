
package com.app.fr.getmymask.helpers.variant

import com.app.fr.getmymask.BuildConfig
import com.app.fr.getmymask.Constants

import javax.inject.Inject

enum class EnvironmentType {
    DEV,
    PROD
}

class VariantHelperImpl @Inject constructor() : VariantHelper {

    private val environment = getEnvironmentType()

    override fun getBackendEndPoint(): String {
        return when (environment) {
            EnvironmentType.DEV -> Constants.DEV_URL
            EnvironmentType.PROD -> Constants.PROD_URL
        }
    }

    private fun getEnvironmentType(): EnvironmentType {

        if (getBuildType() == "debug") {
            return EnvironmentType.DEV
        }
        if (getBuildType() == "release") {
            return EnvironmentType.PROD
        }

        throw Exception("no EnvironmentType find for ${getBuildType()}")
    }

    private fun getBuildType(): String {
        return BuildConfig.BUILD_TYPE
    }
}