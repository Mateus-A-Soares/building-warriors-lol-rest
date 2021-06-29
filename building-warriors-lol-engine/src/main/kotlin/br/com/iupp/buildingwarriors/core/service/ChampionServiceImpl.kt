package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import java.util.*
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepositoryPort) : ChampionServicePort {

    override fun findAll() = championRepository.findAll().map {
        with(it) {
            ChampionResponse(
                name = name,
                shortDescription = shortDescription,
                role = role.toString(),
                difficulty = difficulty.toString()
            )
        }
    }

    override fun findById(id: UUID) = with(championRepository.findById(id)) {
        if (isPresent) with(get()) {
            Optional.of(
                ChampionResponse(
                    name = name,
                    shortDescription = shortDescription,
                    role = role.toString(),
                    difficulty = difficulty.toString()
                )
            )
        }
        else Optional.empty()
    }
}
