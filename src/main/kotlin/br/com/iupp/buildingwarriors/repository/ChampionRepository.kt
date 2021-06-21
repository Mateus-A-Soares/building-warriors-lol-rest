package br.com.iupp.buildingwarriors.repository

import br.com.iupp.buildingwarriors.model.Champion
import java.util.*

interface ChampionRepository {

    fun existsByName(name: String) : Boolean
    fun save(champion: Champion): Champion
    fun findById(id: Long): Optional<Champion>
    fun findAll(): List<Champion>
    fun deleteById(id: Long)
}
