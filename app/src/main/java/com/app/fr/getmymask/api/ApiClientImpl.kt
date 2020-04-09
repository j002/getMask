package com.app.fr.getmymask.api



import com.app.fr.getmymask.Constants
import com.app.fr.getmymask.exceptions.*
import io.reactivex.Emitter

abstract class ApiClientImpl {

    /**
     * observer error Handler to supply to anko async for emit api client error
     */
    fun handleStatusCodeToSendEmitter(emitter: Emitter<*>, statusCode: Int) {
        when (statusCode) {
            Constants.HTTPStatus.NOT_FOUND.code -> onNotFoundRequest(emitter)
            Constants.HTTPStatus.BAD_REQUEST.code -> onBadRequest(emitter)
            Constants.HTTPStatus.BAD_EMAIL.code -> onEmailBadRequest(emitter)
            Constants.HTTPStatus.UNAUTHORIZED.code, Constants.HTTPStatus.FORBIDDEN.code -> onUnauthorizedRequest(emitter)
            Constants.HTTPStatus.INTERNAL_SERVER_ERROR.code -> onServerError(emitter)
        }
    }

    private fun onEmailBadRequest(emitter: Emitter<*>) {
        emitter.onError(BadEmailException())
    }

    private fun onBadRequest(emitter: Emitter<*>) {
        emitter.onError(BadRequestException())
    }

    private fun onUnauthorizedRequest(emitter: Emitter<*>) {
        emitter.onError(UnauthorizedException())
    }

    private fun onServerError(emitter: Emitter<*>) {
        emitter.onError(ServerErrorException())
    }

    private fun onNotFoundRequest(emitter: Emitter<*>) {
        emitter.onError(NotFoundException())
    }
}



