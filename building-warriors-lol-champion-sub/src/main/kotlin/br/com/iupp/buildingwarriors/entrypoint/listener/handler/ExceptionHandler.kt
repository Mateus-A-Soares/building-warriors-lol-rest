package br.com.iupp.buildingwarriors.entrypoint.listener.handler

import br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception.UniqueFieldAlreadyExistsException
import io.micronaut.context.annotation.Requires
import io.micronaut.core.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
@Requires(classes = [Throwable::class, ExceptionHandler::class])
class ExceptionHandler : ExceptionHandler<Throwable> {

    override fun handle(exception: Throwable) {
        when (exception) {
            is UniqueFieldAlreadyExistsException -> println("unique field alreay existis exception")
            is FieldConstraintException -> println("field constraint exception")
            else -> println("unknow exception")
        }
    }
}