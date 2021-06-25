package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse

interface ChampionServicePort {
    fun createRequest(champion: Champion): ChampionResponse
    fun updateRequest(id: String, champion: Champion): ChampionResponse
    fun deleteRequest(id: String)
}