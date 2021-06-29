package br.com.iupp.buildingwarriors.core.mapper

import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty.*
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.core.model.ChampionRole.*
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty as RepositoryDifficulty
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionRole as RepositoryRole

object ChampionMapper {

    fun championEntityToModel(champion: ChampionEntity): Champion = with(champion) {
        Champion(
            id = id,
            name = name,
            shortDescription = shortDescription,
            role = entityRoleToModel(role),
            difficulty = entityDifficultyToModel(difficulty)
        )
    }

    fun entityDifficultyToModel(difficulty: RepositoryDifficulty): ChampionDifficulty = when (difficulty) {
        RepositoryDifficulty.LOW -> LOW
        RepositoryDifficulty.MODERATE -> MODERATE
        RepositoryDifficulty.HIGH -> HIGH
    }

    fun entityRoleToModel(role: RepositoryRole): ChampionRole = when (role) {
        RepositoryRole.ASSASSIN -> ASSASSIN
        RepositoryRole.FIGHTER -> FIGHTER
        RepositoryRole.MAGE -> MAGE
        RepositoryRole.MARKSMAN -> MARKSMAN
        RepositoryRole.SUPPORT -> SUPPORT
        RepositoryRole.TANK -> TANK
    }

    fun stringToChampionRole(value: String): ChampionRole? = try {
        ChampionRole.valueOf(value.toUpperCase())
    } catch (e: IllegalArgumentException) {
        null
    }

    fun stringToChampionDifficulty(value: String): ChampionDifficulty? = try {
        ChampionDifficulty.valueOf(value.toUpperCase())
    } catch (e: IllegalArgumentException) {
        null
    }
}