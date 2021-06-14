package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepository) : ChampionService {

    @Transactional
    override fun saveChampion(championRequest: ChampionRequest): ChampionResponse {
        val champion = championRequest.toModel(championRepository)
        return ChampionResponse(championRepository.save(champion))
    }

    override fun getChampion(id: Long): Optional<ChampionResponse> {
        val optionalChampion = championRepository.findById(id)
        return with(optionalChampion) {
            if (isPresent) Optional.of(ChampionResponse(get()))
            else Optional.empty()
        }
    }

    override fun getAllChampions(): List<ChampionResponse> {
        return championRepository.findAll().map { ChampionResponse(it) }
    }

    @Transactional
    override fun updateChampion(
        id: Long,
        championRequest: ChampionRequest
    ): Optional<ChampionResponse> {
        val optionalChampion = championRepository.findById(id)
        return if (optionalChampion.isEmpty) Optional.empty()
        else {
            with(optionalChampion.get()) {
                if (!championRequest.name.isNullOrBlank()
                    && championRequest.name != name
                    && championRepository.existsByName(championRequest.name)
                ) throw UniqueFieldAlreadyExistsException(entity = "champion", field = "name")
                if (!championRequest.name.isNullOrBlank()) name = championRequest.name
                if (!championRequest.shortDescription.isNullOrBlank()) shortDescription = championRequest.shortDescription
                if (!championRequest.role.isNullOrBlank()) role = ChampionRole.valueOf(championRequest.role.toUpperCase())
                if (!championRequest.difficulty.isNullOrBlank()) difficulty = ChampionDifficulty.valueOf(championRequest.difficulty.toUpperCase())
                Optional.of(ChampionResponse(this))
            }
        }
    }

    @Transactional
    override fun deleteChampion(id: Long) = championRepository.deleteById(id)
}