package br.com.iupp.buildingwarriors.core.model

import java.util.*
import javax.validation.constraints.Size

data class Champion(
    val id: String? = null,
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 255)
    val shortDescription: String? = null,
    val role: ChampionRole? = null,
    val difficulty: ChampionDifficulty? = null
)
