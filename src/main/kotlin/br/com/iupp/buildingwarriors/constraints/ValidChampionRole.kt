package br.com.iupp.buildingwarriors.constraints

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRole
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/**
 * A string precisa ser igual a um dos valores da Enum ChampionRole, independente da caixa das letras.
 * Valor null e considerado valido.
 */
@Target(FIELD, PROPERTY, VALUE_PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidChampionRoleValidator::class])
annotation class ValidChampionRole(
    val message: String = "Deve ser um dos seguintes valores: ASSASSIN, FIGHTER, MAGE, MARKSMAN, SUPPORT, TANK",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
private class ValidChampionRoleValidator : ConstraintValidator<ValidChampionRole, String> {

    override fun isValid(
        championRoleString: String?,
        annotationMetadata: AnnotationValue<ValidChampionRole>,
        context: ConstraintValidatorContext
    ): Boolean {
        return championRoleString?.let {
            ChampionRole.values().any { championRole ->
                championRole.name.equals(it, ignoreCase = true)
            }
        } ?: true
    }
}
