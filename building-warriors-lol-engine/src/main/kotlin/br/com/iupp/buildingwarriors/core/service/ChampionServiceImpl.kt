package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepositoryPort) : ChampionServicePort {

    override fun findAll(): List<ChampionResponse> = championRepository.findAll().map {
        with(it) {
            ChampionResponse(
                name = name,
                shortDescription = shortDescription,
                role = role.toString(),
                difficulty = difficulty.toString()
            )
        }
    }
}
