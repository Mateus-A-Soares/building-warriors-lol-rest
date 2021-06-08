package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionService(@Inject val championRepository: ChampionRepository) {

    fun saveChampion(champion : Champion): Champion {
        championRepository.save(champion)
        return champion
    }
}
