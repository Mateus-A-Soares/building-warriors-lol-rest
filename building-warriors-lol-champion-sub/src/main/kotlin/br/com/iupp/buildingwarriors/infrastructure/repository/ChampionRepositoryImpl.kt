package br.com.iupp.buildingwarriors.infrastructure.repository

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championEntityToModel
import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.cqlRowToChampion
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*
import javax.inject.Singleton

@Singleton
class ChampionRepositoryImpl(private val cqlSession: CqlSession) : ChampionRepositoryPort {

    override fun findById(id: UUID): Optional<Champion> {
        return Optional.ofNullable(
            cqlSession.execute(SimpleStatement.newInstance("SELECT * from champion where id = ?", id)).one()
        ).map(::cqlRowToChampion)
    }

    override fun save(champion: ChampionEntity): Champion {
        champion.id = UUID.randomUUID()
        cqlSession.execute(
            SimpleStatement.newInstance(
                "INSERT INTO champion(id, name, shortDescription, role, difficulty) VALUES (?,?,?,?,?)",
                champion.id,
                champion.name,
                champion.shortDescription,
                champion.role.toString(),
                champion.difficulty.toString()
            )
        )
        return championEntityToModel(champion)
    }

    override fun update(champion: ChampionEntity): Champion {
        cqlSession.execute(
            SimpleStatement.newInstance(
                "UPDATE champion SET name = ?, shortDescription = ?, role = ?, difficulty = ? WHERE ID = ?",
                champion.name,
                champion.shortDescription,
                champion.role.toString(),
                champion.difficulty.toString(),
                champion.id
            )
        )
        return findById(champion.id!!).orElseThrow { RuntimeException() }
    }

    override fun deleteById(id: UUID) {
        cqlSession.execute(SimpleStatement.newInstance("DELETE from champion where id = ?", id))
    }
}
