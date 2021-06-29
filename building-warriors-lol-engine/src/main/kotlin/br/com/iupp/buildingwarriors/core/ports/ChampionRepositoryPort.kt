package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.core.model.Champion

interface ChampionRepositoryPort {

    fun findAll(): List<Champion>
}
