package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionService {

    fun saveChampion(champion: Champion): ChampionCreatedResponse
    fun getChampion(id: Long): Optional<ChampionDetailsResponse>
    fun updateChampion(id: Long, updateRequest: UpdateChampionRequest): Optional<ChampionDetailsResponse>
    fun deleteChampion(id: Long)
}