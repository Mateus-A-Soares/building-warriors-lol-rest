package br.com.iupp.buildingwarriors.controller.champion.response

import br.com.iupp.buildingwarriors.controller.champion.response.ConstraintErrorDto.Embedded.ErrorMessage

internal data class ConstraintErrorDto(
    val message: String?,
    val _embedded: Embedded?
) {
    val errors: List<ErrorMessage?>?
        get() = _embedded?.errors

    data class Embedded(val errors: List<ErrorMessage?>?) {
        data class ErrorMessage(val message: String?)
    }
}