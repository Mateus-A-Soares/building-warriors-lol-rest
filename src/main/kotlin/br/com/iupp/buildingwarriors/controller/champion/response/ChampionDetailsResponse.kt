package br.com.iupp.buildingwarriors.controller.champion.response

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole

data class ChampionDetailsResponse(
    val id: Long?,
    val name: String,
    val shortDescription: String,
    val role: ChampionRole,
    val difficulty: ChampionDifficulty
) {
    constructor(champion: Champion) : this(
        id = champion.id,
        name = champion.name,
        shortDescription = champion.shortDescription,
        role = champion.role,
        difficulty = champion.difficulty
    )
}
