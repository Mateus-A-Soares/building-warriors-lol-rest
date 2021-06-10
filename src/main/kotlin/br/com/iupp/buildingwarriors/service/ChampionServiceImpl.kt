package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChampionServiceImpl(@Inject val championRepository: ChampionRepository) : ChampionService {

    override fun saveChampion(champion: Champion): Champion {
        championRepository.save(champion)
        return champion
    }

    override fun getChampion(id: Long): Optional<Champion> {
        return championRepository.findById(id)
    }

    override fun updateChampion(id: Long, updateChampionRequest: UpdateChampionRequest): Optional<Champion> {
        val champion = championRepository.findById(id)
        with(champion) {
            if (isEmpty) return champion
            val updatedChampion = with(updateChampionRequest) {
                if (!name.isNullOrBlank() && get().name != name && championRepository.existsByName(name))
                    throw UniqueFieldAlreadyExistsException(entity = "champion", field = "name")
                Champion(
                    name = if (name.isNullOrBlank()) get().name else name,
                    shortDescription = if (shortDescription.isNullOrBlank()) get().shortDescription else shortDescription,
                    role = if (role.isNullOrBlank()) get().role else ChampionRole.valueOf(role),
                    difficulty = if (difficulty.isNullOrBlank()) get().difficulty else ChampionDifficulty.valueOf(
                        difficulty
                    )
                )
            }
            updatedChampion.id = get().id
            return Optional.of(championRepository.update(updatedChampion))
        }
    }
}
