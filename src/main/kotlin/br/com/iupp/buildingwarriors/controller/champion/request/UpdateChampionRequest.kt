package br.com.iupp.buildingwarriors.controller.champion.request

import br.com.iupp.buildingwarriors.constraints.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.constraints.ValidChampionRole
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

@Introspected
data class UpdateChampionRequest(
    @field:Size(max = 50)
    val name: String?,
    @field:Size(max = 255)
    val shortDescription: String?,
    @field:ValidChampionRole
    val role: String?,
    @field:ValidChampionDifficulty
    val difficulty: String?
)
