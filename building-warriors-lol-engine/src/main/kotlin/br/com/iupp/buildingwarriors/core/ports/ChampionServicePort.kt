package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import java.util.*

interface ChampionServicePort {

    fun findAll() : List<ChampionResponse>
    fun findById(id: UUID): Optional<ChampionResponse>
}