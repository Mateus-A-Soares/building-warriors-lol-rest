package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionService {

    fun saveChampion(champion: Champion): ChampionResponse
    fun getChampion(id: Long): Optional<ChampionResponse>
    fun updateChampion(id: Long, updateRequest: UpdateChampionRequest): Optional<ChampionResponse>
    fun deleteChampion(id: Long)
    fun getAllChampions(): List<ChampionResponse>
}