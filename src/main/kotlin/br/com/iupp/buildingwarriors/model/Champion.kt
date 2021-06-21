package br.com.iupp.buildingwarriors.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class Champion(
    @field: NotBlank @field:Size(max = 50)
    var name: String,

    @field:NotBlank @field:Size(max = 255)
    var shortDescription: String,

    @field:NotBlank
    var role: ChampionRole,

    @field:NotBlank
    var difficulty: ChampionDifficulty,

    var id : Long? = null
)
