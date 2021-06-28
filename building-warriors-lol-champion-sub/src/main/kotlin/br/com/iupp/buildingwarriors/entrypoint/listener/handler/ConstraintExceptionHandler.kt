package br.com.iupp.buildingwarriors.entrypoint.listener.handler

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.exceptions.ExceptionHandler
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@Requires(classes = [ConstraintViolationException::class, ExceptionHandler::class])
@Replaces(ConstraintExceptionHandler::class)
class ConstraintExceptionHandler : ExceptionHandler<ConstraintViolationException> {

    override fun handle(
        exception: ConstraintViolationException
    ) {
        val messages = exception.constraintViolations.map {
            "${it.propertyPath.last()}: ${it.message}"
        }
        messages.let(::println)
    }
}