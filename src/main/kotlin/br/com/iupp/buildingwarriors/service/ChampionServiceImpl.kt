package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.exception.FieldConstraintException
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
    override fun saveChampion(request: ChampionRequest): ChampionResponse {
        val champion = request.toModel(championRepository)
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
        request: ChampionRequest
    ): Optional<ChampionResponse> {
        val optionalChampion = championRepository.findById(id)
        return if (optionalChampion.isEmpty) Optional.empty()
        else {
            with(optionalChampion.get()) {
                if (!request.name.isNullOrBlank()
                    && request.name != name
                    && championRepository.existsByName(request.name!!)
                ) throw UniqueFieldAlreadyExistsException(entity = "champion", field = "name")
                if (!request.name.isNullOrBlank()) name = request.name!!
                if (!request.shortDescription.isNullOrBlank()) shortDescription = request.shortDescription!!
                if (!request.role.isNullOrBlank()) role = ChampionRole.valueOf(request.role!!.toUpperCase())
                if (!request.difficulty.isNullOrBlank()) difficulty = ChampionDifficulty.valueOf(request.difficulty!!.toUpperCase())
                Optional.of(ChampionResponse(this))
            }
        }
    }

    @Transactional
    override fun deleteChampion(id: Long) = championRepository.deleteById(id)
}