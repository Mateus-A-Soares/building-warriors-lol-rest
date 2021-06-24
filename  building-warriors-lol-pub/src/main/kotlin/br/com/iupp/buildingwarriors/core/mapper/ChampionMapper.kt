package br.com.iupp.buildingwarriors.core.mapper

import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.entrypoint.controller.request.ChampionRequest
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEvent

object ChampionMapper {
    fun championRequestToChampion(championRequest: ChampionRequest): Champion =
        with(championRequest) {
            Champion(
                id = id,
                name = name,
                shortDescription = shortDescription,
                role = role?.let(::stringToChampionRole),
                difficulty = difficulty?.let(::stringToChampionDifficulty)
            )
        }

    fun championToChampionResponse(champion: Champion): ChampionResponse =with(champion){
        ChampionResponse(
            id = id,
            name = name,
            shortDescription = shortDescription,
            role = role.toString(),
            difficulty = difficulty.toString()
        )
    }

    private fun stringToChampionRole(value: String): ChampionRole? = try {
        ChampionRole.valueOf(value.toUpperCase())
    } catch (e: IllegalArgumentException) {
        null
    }

    private fun stringToChampionDifficulty(value: String): ChampionDifficulty? = try {
        ChampionDifficulty.valueOf(value.toUpperCase())
    } catch (e: IllegalArgumentException) {
        null
    }

    fun championToChampionEvent(champion: Champion): ChampionEvent = with(champion){
        ChampionEvent(
            id = id,
            name = name,
            shortDescription = shortDescription,
            role = role?.toString(),
            difficulty = difficulty?.toString()
        )
    }
}
