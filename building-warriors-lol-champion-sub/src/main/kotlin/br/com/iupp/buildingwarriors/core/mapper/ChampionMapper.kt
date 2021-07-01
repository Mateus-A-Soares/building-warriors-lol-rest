package br.com.iupp.buildingwarriors.core.mapper

import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty.*
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.core.model.ChampionRole.*
import br.com.iupp.buildingwarriors.entrypoint.listener.request.ChampionRequest
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import com.datastax.oss.driver.api.core.cql.Row
import java.util.*
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty as RepositoryDifficulty
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionRole as RepositoryRole

object ChampionMapper {

    fun championToEntity(champion: Champion): ChampionEntity = with(champion) {
        ChampionEntity(
            id = id,
            name = name!!,
            shortDescription = shortDescription!!,
            role = roleToEntity(role!!),
            difficulty = difficultyToEntity(difficulty!!)
        )
    }

    fun difficultyToEntity(difficulty: ChampionDifficulty): RepositoryDifficulty = when (difficulty) {
        LOW -> RepositoryDifficulty.LOW
        MODERATE -> RepositoryDifficulty.MODERATE
        HIGH -> RepositoryDifficulty.HIGH
    }

    fun roleToEntity(role: ChampionRole): RepositoryRole = when (role) {
        ASSASSIN -> RepositoryRole.ASSASSIN
        FIGHTER -> RepositoryRole.FIGHTER
        MAGE -> RepositoryRole.MAGE
        MARKSMAN -> RepositoryRole.MARKSMAN
        SUPPORT -> RepositoryRole.SUPPORT
        TANK -> RepositoryRole.TANK
    }

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

    fun championEventToModel(championRequest: ChampionRequest): Champion = with(championRequest){
        Champion(
            id = id?.let(UUID::fromString),
            name = name,
            shortDescription = shortDescription,
            role = role?.let (::stringToChampionRole),
            difficulty = difficulty?.let(::stringToChampionDifficulty)
        )
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

    fun cqlRowToChampion(row: Row): Champion {
        return Champion(
            id = row.getUuid("id"),
            name = row.getString("name")!!,
            shortDescription = row.getString("shortDescription")!!,
            role = row.getString("role")?.let(ChampionMapper::stringToChampionRole),
            difficulty = row.getString("difficulty")?.let(ChampionMapper::stringToChampionDifficulty)
        )
    }
}