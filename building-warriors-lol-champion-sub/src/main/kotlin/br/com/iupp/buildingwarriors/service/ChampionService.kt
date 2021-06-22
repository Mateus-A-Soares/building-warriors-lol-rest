package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import java.util.*

interface ChampionService {

    fun saveChampion(championRequest: ChampionRequest): ChampionResponse
    fun getChampion(id: String): Optional<ChampionResponse>
    fun updateChampion(id: String, championRequest: ChampionRequest): Optional<ChampionResponse>
    fun deleteChampion(id: String)
    fun getAllChampions(): List<ChampionResponse>
}