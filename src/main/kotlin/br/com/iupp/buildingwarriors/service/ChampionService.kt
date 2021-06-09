package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.model.Champion

interface ChampionService {

    fun saveChampion(champion: Champion): Champion
}