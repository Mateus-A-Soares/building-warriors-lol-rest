package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.core.model.Champion

interface ChampionServicePort {

    fun saveChampion(champion: Champion)
    fun updateChampion(champion: Champion)
    fun deleteChampion(champion: Champion)
}