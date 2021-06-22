package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import java.util.*
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepository) : ChampionService {

    override fun saveChampion(championRequest: ChampionRequest): ChampionResponse {
        val champion = championRequest.toModel()
        return ChampionResponse(championRepository.save(champion))
    }

    override fun getChampion(id: String): Optional<ChampionResponse> {
        val optionalChampion = championRepository.findById(UUID.fromString(id))
        return with(optionalChampion) {
            if (isPresent) Optional.of(ChampionResponse(get()))
            else Optional.empty()
        }
    }

    override fun getAllChampions(): List<ChampionResponse> {
        return championRepository.findAll().toList().map { ChampionResponse(it) }
    }

    override fun updateChampion(
        id: String,
        championRequest: ChampionRequest
    ): Optional<ChampionResponse> {
        val optionalChampion = championRepository.findById(UUID.fromString(id))
        return if (optionalChampion.isEmpty) Optional.empty()
        else {
            with(optionalChampion.get()) {
                if (!championRequest.name.isNullOrBlank()) name = championRequest.name
                if (!championRequest.shortDescription.isNullOrBlank()) shortDescription = championRequest.shortDescription
                if (!championRequest.role.isNullOrBlank()) role = ChampionRole.valueOf(championRequest.role.toUpperCase())
                if (!championRequest.difficulty.isNullOrBlank()) difficulty = ChampionDifficulty.valueOf(championRequest.difficulty.toUpperCase())
                Optional.of(ChampionResponse(championRepository.update(this)))
            }
        }
    }

    override fun deleteChampion(id: String) = championRepository.deleteById(UUID.fromString(id))
}