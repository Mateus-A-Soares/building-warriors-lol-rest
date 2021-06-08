package br.com.iupp.buildingwarriors.controller.champion.request

data class CreateChampionRequest(
    val name: String,
    val shortDescription: String,
    val role: ChampionRole,
    val difficulty: ChampionDifficulty
)

enum class ChampionRole {
    ASSASSIN,
    FIGHTER,
    MAGE,
    MARKSMAN,
    SUPPORT,
    TANK
}

enum class ChampionDifficulty {
    LOW, MODERATE, HIGH
}
