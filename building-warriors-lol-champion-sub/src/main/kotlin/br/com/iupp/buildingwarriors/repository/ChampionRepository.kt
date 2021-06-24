package br.com.iupp.buildingwarriors.repository

import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionRepository {

    fun save(champion: Champion): Champion
    fun findById(id: UUID): Optional<Champion>
    fun findAll(): List<Champion>
    fun deleteById(id: UUID)
    fun update(champion: Champion): Champion
    fun delete(toModel: Champion)
}
