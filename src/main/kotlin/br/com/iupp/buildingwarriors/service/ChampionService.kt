package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import java.util.*

interface ChampionService {

    fun saveChampion(championRequest: ChampionRequest): ChampionResponse
    fun getChampion(id: Long): Optional<ChampionResponse>
    fun updateChampion(id: Long, championRequest: ChampionRequest): Optional<ChampionResponse>
    fun deleteChampion(id: Long)
    fun getAllChampions(): List<ChampionResponse>
}