package br.com.iupp.buildingwarriors.infrastructure.service

import br.com.iupp.buildingwarriors.core.ports.NatsServicePort
import br.com.iupp.buildingwarriors.infrastructure.client.ChampionClient
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEvent
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEventInformation
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionOperations.*
import javax.inject.Singleton

@Singleton
class NatsService(val client : ChampionClient) : NatsServicePort {

    override fun createChampionEvent(championEvent: ChampionEvent) {
        client.publishEvent(ChampionEventInformation(CREATE, championEvent.apply { id = null }))
    }

    override fun updateChampionEvent(championEvent: ChampionEvent) {
        client.publishEvent(ChampionEventInformation(UPDATE, championEvent))
    }

    override fun deleteChampionEvent(id: String) {
        client.publishEvent(ChampionEventInformation(DELETE, ChampionEvent(id = id)))
    }
}