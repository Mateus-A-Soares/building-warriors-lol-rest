package br.com.iupp.buildingwarriors.constraints

import br.com.iupp.buildingwarriors.repository.ChampionRepository
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 *  Verifica se o campo name na tabela Champion não tem o valor no parâmetro anotado.
 * Valor null e considerado invalido.
 */
@Target(FIELD, PROPERTY, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [UniqueChampionNameValidator::class])
annotation class UniqueChampionName(
    val message: String = "O nome deve ser único",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
private class UniqueChampionNameValidator(@Inject val championRepository: ChampionRepository) :
    ConstraintValidator<UniqueChampionName, String> {

    override fun isValid(
        championName: String?,
        annotationMetadata: AnnotationValue<UniqueChampionName>,
        context: ConstraintValidatorContext
    ): Boolean {
        return championName?.let {
            !championRepository.existsByName(it)
        } ?: false
    }
}
