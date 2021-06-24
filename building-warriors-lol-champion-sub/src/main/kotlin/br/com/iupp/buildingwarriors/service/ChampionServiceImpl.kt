package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.listener.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.exception.EntityNotFound
import br.com.iupp.buildingwarriors.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import java.util.*
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepository) : ChampionService {

    override fun saveChampion(championRequest: ChampionRequest) {
        championRepository.save(championRequest.toModel())
    }

    override fun updateChampion(
        championRequest: ChampionRequest
    ) {
        with(championRequest) {
            if (id.isNullOrBlank())
                throw FieldConstraintException(entity = "champion", listOf("id" to "id não foi informado"))
            val optionalChampion = championRepository.findById(UUID.fromString(id))
            if (optionalChampion.isEmpty) throw EntityNotFound("champion")
            optionalChampion.get().apply {
                if (!this@with.name.isNullOrBlank()) name = this@with.name
                if (!this@with.shortDescription.isNullOrBlank()) shortDescription = this@with.shortDescription
                if (!this@with.role.isNullOrBlank()) role = ChampionRole.valueOf(this@with.role.toUpperCase())
                if (!this@with.difficulty.isNullOrBlank()) difficulty =
                    ChampionDifficulty.valueOf(this@with.difficulty.toUpperCase())
                championRepository.update(this)
            }
        }
    }

    override fun deleteChampion(championRequest: ChampionRequest) = championRepository.delete(championRequest.toModel())
}