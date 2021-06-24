package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToChampionResponse
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import javax.inject.Singleton

@Singleton
class ChampionService: ChampionServicePort {

    override fun createRequest(champion: Champion): ChampionResponse = championToChampionResponse(champion)

    override fun updateRequest(champion: Champion): ChampionResponse = championToChampionResponse(champion)

    override fun deleteRequest(id: String) {
    }
}