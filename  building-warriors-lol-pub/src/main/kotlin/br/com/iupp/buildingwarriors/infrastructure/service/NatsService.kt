package br.com.iupp.buildingwarriors.infrastructure.service

import br.com.iupp.buildingwarriors.core.ports.NatsServicePort
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEvent
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEventInformation
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionOperations.*
import javax.inject.Singleton

@Singleton
class NatsService : NatsServicePort {

    override fun createChampionEvent(championEvent: ChampionEvent) {
        ChampionEventInformation(CREATE, championEvent.apply { id = null }).toString().let(::println)
    }

    override fun updateChampionEvent(championEvent: ChampionEvent) {
        ChampionEventInformation(UPDATE, championEvent).toString().let(::println)
    }

    override fun deleteChampionEvent(id: String) {
        ChampionEventInformation(DELETE, ChampionEvent(id = id)).toString().let(::println)
    }
}