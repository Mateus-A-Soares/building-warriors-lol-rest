package br.com.iupp.buildingwarriors.infrastructure.repository

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*
import java.util.stream.Collectors
import javax.inject.Singleton

@Singleton
class ChampionRepositoryImpl(private val cqlSession: CqlSession) : ChampionRepositoryPort {

    override fun findById(id: UUID): Optional<Champion> {
        return Optional.ofNullable(
            cqlSession.execute(SimpleStatement.newInstance("SELECT * from champion where id = ?", id)).one()
        ).map(this::mapToChampion)
    }

    override fun findAll(): List<Champion> {
        val result = cqlSession.execute(SimpleStatement.newInstance("SELECT * FROM champion"))
        return result.all().stream().map(::mapToChampion).collect(Collectors.toList())
    }

    private fun mapToChampion(row: Row): Champion {
        return Champion(
            id = row.getUuid("id"),
            name = row.getString("name")!!,
            shortDescription = row.getString("shortDescription")!!,
            role = row.getString("role")?.let(ChampionMapper::stringToChampionRole),
            difficulty = row.getString("difficulty")?.let(ChampionMapper::stringToChampionDifficulty)
        )
    }
}
