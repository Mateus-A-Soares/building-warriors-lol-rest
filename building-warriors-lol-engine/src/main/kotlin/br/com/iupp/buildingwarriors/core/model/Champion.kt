package br.com.iupp.buildingwarriors.core.model

import java.util.*

data class Champion(
    var id : UUID? = null,
    var name: String? = null,
    var shortDescription: String? = null,
    var role: ChampionRole? = null,
    var difficulty: ChampionDifficulty? = null,
)
