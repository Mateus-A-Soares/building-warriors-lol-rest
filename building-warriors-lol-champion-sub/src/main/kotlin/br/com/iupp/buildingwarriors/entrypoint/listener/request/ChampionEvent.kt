package br.com.iupp.buildingwarriors.entrypoint.listener.request

data class ChampionEvent(
    val operation: Operation,
    val championRequest: ChampionRequest
)

enum class Operation(val eventName : String) {
    CREATE("CREATE"), UPDATE("UPDATE"), DELETE("DELETE")
}
