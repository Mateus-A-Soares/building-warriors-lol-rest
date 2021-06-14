package br.com.iupp.buildingwarriors.controller.champion.request

import br.com.iupp.buildingwarriors.util.validator.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.util.validator.ValidChampionRole
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

@Introspected
data class UpdateChampionRequest(
    @field:Size(max = 50)
    var name: String? = null,
    @field:Size(max = 255)
    var shortDescription: String? = null,
    @field:ValidChampionRole
    var role: String? = null,
    @field:ValidChampionDifficulty
    var difficulty: String? = null
)
