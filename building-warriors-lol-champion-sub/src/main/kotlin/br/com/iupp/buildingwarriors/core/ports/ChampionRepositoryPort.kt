package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import java.util.*

interface ChampionRepositoryPort {

    fun save(champion: ChampionEntity): Champion
    fun findById(id: UUID): Optional<Champion>
    fun findAll(): List<Champion>
    fun deleteById(id: UUID)
    fun update(champion: ChampionEntity): Champion
    fun delete(champion: ChampionEntity)
    fun delete(id: UUID)
}
