package br.com.iupp.buildingwarriors.listener.handler

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@Requires(classes = [ConstraintViolationException::class, ExceptionHandler::class])
@Replaces(io.micronaut.validation.exceptions.ConstraintExceptionHandler::class)
class ConstraintExceptionHandler : ExceptionHandler<ConstraintViolationException, Unit> {

    override fun handle(
        request: HttpRequest<*>,
        exception: ConstraintViolationException
    ) {
        val messages = exception.constraintViolations.map {
            "${it.propertyPath.last()}: ${it.message}"
        }
    }
}