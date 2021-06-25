package br.com.iupp.buildingwarriors.entrypoint.controller.handler

import br.com.iupp.buildingwarriors.entrypoint.controller.handler.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.entrypoint.controller.response.DefaultErrorResponse
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.UNPROCESSABLE_ENTITY
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [Throwable::class, ExceptionHandler::class])
class ExceptionHandler : ExceptionHandler<Throwable, HttpResponse<DefaultErrorResponse?>> {

    override fun handle(request: HttpRequest<*>, exception: Throwable): HttpResponse<DefaultErrorResponse?> {
        return when (exception) {
            is FieldConstraintException -> HttpResponse.unprocessableEntity<DefaultErrorResponse>().body(
                DefaultErrorResponse(
                    statusCode = UNPROCESSABLE_ENTITY.code,
                    error = UNPROCESSABLE_ENTITY.reason,
                    messages = exception.fieldErrors.map { "${it.first}: ${it.second}" },
                    path = request.path
                )
            )
            else -> {
                exception.printStackTrace()
                HttpResponse.serverError<DefaultErrorResponse>().body(
                    DefaultErrorResponse(
                        statusCode = HttpStatus.INTERNAL_SERVER_ERROR.code,
                        error = HttpStatus.INTERNAL_SERVER_ERROR.reason,
                        messages = listOf("Desconhecido"),
                        path = request.path
                    )
                )
            }
        }
    }
}