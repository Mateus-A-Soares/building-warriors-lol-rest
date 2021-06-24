package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest

interface ChampionService {

    fun saveChampion(championRequest: ChampionRequest)
    fun updateChampion(championRequest: ChampionRequest)
    fun deleteChampion(championRequest: ChampionRequest)
}