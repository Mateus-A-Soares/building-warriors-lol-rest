package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionService {

    fun saveChampion(champion: Champion): Champion
    fun getChampion(id: Long): Optional<Champion>
    fun updateChampion(id: Long, updateChampionRequest: UpdateChampionRequest): Optional<Champion>
}