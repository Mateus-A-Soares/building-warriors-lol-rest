package br.com.iupp.buildingwarriors.core.ports

import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEvent

interface NatsServicePort {
    fun createChampionEvent(championToChampionEvent: ChampionEvent)
    fun updateChampionEvent(championToChampionEvent: ChampionEvent)
    fun deleteChampionEvent(id: String)
}
