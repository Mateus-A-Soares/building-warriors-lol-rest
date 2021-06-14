package br.com.iupp.buildingwarriors.controller.handler

import br.com.iupp.buildingwarriors.controller.handler.response.DefaultErrorResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [Throwable::class, ExceptionHandler::class])
class ExceptionHandler : ExceptionHandler<Throwable, HttpResponse<DefaultErrorResponse?>> {

    override fun handle(request: HttpRequest<*>, exception: Throwable): HttpResponse<DefaultErrorResponse?> {
        return when (exception) {
            is UniqueFieldAlreadyExistsException -> HttpResponse.unprocessableEntity<DefaultErrorResponse>().body(
                DefaultErrorResponse(
                    statusCode = HttpStatus.UNPROCESSABLE_ENTITY.code,
                    error = HttpStatus.UNPROCESSABLE_ENTITY.reason,
                    messages = listOf("Valor já cadastrado para o campo ${exception.field} nos registros da entidade ${exception.entity}"),
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