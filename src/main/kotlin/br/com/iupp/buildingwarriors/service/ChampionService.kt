package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionService {

    fun saveChampion(champion: Champion): Champion
    fun getChampion(id: Long): Optional<Champion>
}