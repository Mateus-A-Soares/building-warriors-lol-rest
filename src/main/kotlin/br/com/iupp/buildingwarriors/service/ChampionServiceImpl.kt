package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepository) : ChampionService {

    @Transactional
    override fun saveChampion(champion: Champion): ChampionCreatedResponse =
        ChampionCreatedResponse(championRepository.save(champion))

    override fun getChampion(id: Long): Optional<ChampionDetailsResponse> {
        val optionalChampion = championRepository.findById(id)
        return with(optionalChampion) {
            if (isPresent) Optional.of(ChampionDetailsResponse(get()))
            else Optional.empty()
        }
    }

    override fun getAllChampions(): List<ChampionDetailsResponse> {
        return championRepository.findAll().map { ChampionDetailsResponse(it) }
    }

    @Transactional
    override fun updateChampion(
        id: Long,
        updateRequest: UpdateChampionRequest
    ): Optional<ChampionDetailsResponse> {
        val optionalChampion = championRepository.findById(id)
        return if (optionalChampion.isEmpty) Optional.empty()
        else {
            with(optionalChampion.get()) {
                if (!updateRequest.name.isNullOrBlank()
                    && updateRequest.name != name
                    && championRepository.existsByName(updateRequest.name!!)
                ) throw UniqueFieldAlreadyExistsException(entity = "champion", field = "name")
                if (!updateRequest.name.isNullOrBlank()) name = updateRequest.name!!
                if (!updateRequest.shortDescription.isNullOrBlank()) shortDescription = updateRequest.shortDescription!!
                if (!updateRequest.role.isNullOrBlank()) role = ChampionRole.valueOf(updateRequest.role!!.toUpperCase())
                if (!updateRequest.difficulty.isNullOrBlank()) difficulty = ChampionDifficulty.valueOf(updateRequest.difficulty!!.toUpperCase())
                Optional.of(ChampionDetailsResponse(this))
            }
        }
    }

    @Transactional
    override fun deleteChampion(id: Long) = championRepository.deleteById(id)
}