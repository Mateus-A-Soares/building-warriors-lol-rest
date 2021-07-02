package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToResponse
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import java.util.*
import javax.inject.Singleton

@Singleton
open class ChampionServiceImpl(private val championRepository: ChampionRepositoryPort) : ChampionServicePort {

    override fun findAll() = championRepository.findAll().map(::championToResponse)

    override fun findById(id: UUID) = with(championRepository.findById(id)) {
        if (isPresent) Optional.of(championToResponse(get()))
        else Optional.empty()
    }
}
