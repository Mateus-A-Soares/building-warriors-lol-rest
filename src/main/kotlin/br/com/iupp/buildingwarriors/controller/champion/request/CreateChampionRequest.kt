package br.com.iupp.buildingwarriors.controller.champion.request

import br.com.iupp.buildingwarriors.constraints.UniqueChampionName
import br.com.iupp.buildingwarriors.constraints.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.constraints.ValidChampionRole
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class CreateChampionRequest(
    @field:NotBlank @field:Size(max = 50) @field:UniqueChampionName
    val name: String?,
    @field:NotBlank @field:Size(max = 255)
    val shortDescription: String?,
    @field:NotBlank @field:ValidChampionRole
    val role: String?,
    @field:NotBlank @field:ValidChampionDifficulty
    val difficulty: String?
) {

    fun toModel(): Champion {
        return Champion(
            name = name!!,
            shortDescription = shortDescription!!,
            role = ChampionRole.valueOf(role!!.toUpperCase()),
            difficulty = ChampionDifficulty.valueOf(difficulty!!.toUpperCase())
        )
    }
}
