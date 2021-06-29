package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.core.model.Champion
import java.util.*

interface ChampionRepositoryPort {

    fun findAll(): List<Champion>
    fun findById(id: UUID): Optional<Champion>
}
