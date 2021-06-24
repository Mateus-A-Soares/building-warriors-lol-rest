package br.com.iupp.buildingwarriors.controller.handler

import br.com.iupp.buildingwarriors.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
@Requires(classes = [Throwable::class, ExceptionHandler::class])
class ExceptionHandler : ExceptionHandler<Throwable, Unit> {

    override fun handle(request: HttpRequest<*>, exception: Throwable) {
        when (exception) {
            is UniqueFieldAlreadyExistsException -> println("unique field alreay existis exception")
            is FieldConstraintException -> println("field constraint exception")
            else -> println("unknow exception")
        }
    }
}