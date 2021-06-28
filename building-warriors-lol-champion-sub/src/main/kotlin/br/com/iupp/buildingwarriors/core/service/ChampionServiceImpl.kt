package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception.EntityNotFound
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepositoryPort) : ChampionServicePort {

    override fun saveChampion(champion: Champion) {
        championRepository.save(ChampionMapper.championToEntity(champion))
    }

    override fun updateChampion(
        champion: Champion
    ) {
        with(champion) {
            val optionalChampion = championRepository.findById(id!!)
            if (optionalChampion.isEmpty) throw EntityNotFound("champion")
            optionalChampion.get().apply {
                if (!this@with.name.isNullOrBlank()) name = this@with.name
                if (!this@with.shortDescription.isNullOrBlank()) shortDescription = this@with.shortDescription
                if (this@with.role != null) role = this@with.role
                if (this@with.difficulty != null) difficulty = this@with.difficulty
                championRepository.update(ChampionMapper.championToEntity(this))
            }
        }
    }

    override fun deleteChampion(champion: Champion) =
        championRepository.delete(champion.id!!)
}