package br.com.iupp.buildingwarriors.controller.champion.request

import br.com.iupp.buildingwarriors.constraints.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.constraints.ValidChampionRole
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class CreateChampionRequest(
    @field:NotBlank @field:Size(max = 50)
    val name: String?,
    @field:NotBlank @field:Size(max = 255)
    val shortDescription: String?,
    @field:NotBlank @field:ValidChampionRole
    val role: String?,
    @field:NotBlank @field:ValidChampionDifficulty
    val difficulty: String?
)
