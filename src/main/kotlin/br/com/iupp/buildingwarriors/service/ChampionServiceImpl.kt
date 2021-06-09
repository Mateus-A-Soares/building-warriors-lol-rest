package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionServiceImpl(@Inject val championRepository: ChampionRepository) : ChampionService {

    override fun saveChampion(champion : Champion): Champion {
        championRepository.save(champion)
        return champion
    }
}
