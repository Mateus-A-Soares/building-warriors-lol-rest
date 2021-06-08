package br.com.iupp.buildingwarriors.controller.champion.response

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole

class ChampionCreatedResponse(
    champion: Champion,
    val id: Long? = champion.id,
    val name: String = champion.name,
    val shortDescription: String = champion.shortDescription,
    val role: ChampionRole = champion.role,
    val difficulty: ChampionDifficulty = champion.difficulty
)
