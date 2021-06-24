package br.com.iupp.buildingwarriors.model

import br.com.iupp.buildingwarriors.listener.champion.request.ChampionRequest

data class ChampionEvent(
    val operation: Operation,
    val championRequest: ChampionRequest
)

enum class Operation(val eventName : String) {
    CREATE("CREATE"), UPDATE("UPDATE"), DELETE("DELETE")
}
