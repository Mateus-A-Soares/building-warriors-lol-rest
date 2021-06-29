package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse

interface ChampionServicePort {

    fun findAll() : List<ChampionResponse>
}