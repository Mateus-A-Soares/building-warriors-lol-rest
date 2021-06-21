package br.com.iupp.buildingwarriors.repository

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*
import java.util.stream.Collectors
import javax.inject.Singleton

@Singleton
class ChampionRepositoryImpl(private val cqlSession: CqlSession) : ChampionRepository {

    override fun findById(id: UUID): Optional<Champion> {
        return Optional.ofNullable(
            cqlSession.execute(SimpleStatement.newInstance("SELECT * from champion where id = ?", id)).one()
        ).map(this::mapToChampion)
    }

    override fun save(champion: Champion): Champion {
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
        return champion
    }

    override fun deleteById(id: UUID) {
        cqlSession.execute(SimpleStatement.newInstance("DELETE from champion where id = ?", id))
    }

    override fun findAll(): List<Champion> {
        val result = cqlSession.execute(SimpleStatement.newInstance("SELECT * FROM champion"))
        return result.all().stream().map(this::mapToChampion).collect(Collectors.toList())
    }

    override fun update(champion: Champion): Champion {
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


    private fun mapToChampion(row: Row): Champion {
        return Champion(
            id = row.getUuid("id"),
            name = row.getString("name")!!,
            shortDescription = row.getString("shortDescription")!!,
            role = ChampionRole.valueOf(row.getString("role")!!),
            difficulty = ChampionDifficulty.valueOf((row.getString("difficulty")!!))
        )
    }
}
