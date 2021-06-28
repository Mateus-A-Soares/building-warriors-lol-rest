package br.com.iupp.buildingwarriors.entrypoint.listener.validator

import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * A string precisa ser igual a um dos valores da Enum ChampionDifficulty, independente da caixa das letras.
 * Valor null e considerado valido.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidChampionDifficultyValidator::class])
annotation class ValidChampionDifficulty(
    val message: String = "Deve ser um dos seguintes valores: LOW, MODERATE, HIGH",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
private class ValidChampionDifficultyValidator : ConstraintValidator<ValidChampionDifficulty, String> {

    override fun isValid(
        difficultyString: String?,
        annotationMetadata: AnnotationValue<ValidChampionDifficulty>,
        context: ConstraintValidatorContext
    ): Boolean {
        return difficultyString?.let {
            ChampionDifficulty.values().any { difficulty ->
                difficulty.name.equals(it, ignoreCase = true)
            }
        } ?: true
    }
}
