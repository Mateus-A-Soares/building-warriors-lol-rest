package br.com.iupp.buildingwarriors.infrastructure.repository

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championEntityToModel
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionRole
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionRepositoryImplTests : AnnotationSpec() {
    private val cqlSession: CqlSession = mockk<CqlSession>(relaxed = true)
    private val championRepositoryImpl = ChampionRepositoryImpl(cqlSession)
    private val row: Row = mockk<Row>()
    private val champion = ChampionEntity(
        id = UUID.randomUUID(),
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE,
        difficulty = ChampionDifficulty.MODERATE
    )

    @BeforeAll
    fun setup() {
        champion.apply {
            every { row.getUuid("id") } returns id
            every { row.getString("name") } returns name
            every { row.getString("shortDescription") } returns shortDescription
            every { row.getString("role") } returns role.toString()
            every { row.getString("difficulty") } returns difficulty.toString()
        }
        every {
            cqlSession.execute(
                SimpleStatement.newInstance(
                    "SELECT * from champion where id = ?",
                    champion.id
                )
            ).one()
        } returns row
    }

    @Test
    fun `deve retornar Champion encontrado`() {
        val result = championRepositoryImpl.findById(champion.id!!)

        result.isPresent shouldBe true
        result.get() shouldBe championEntityToModel(champion)
    }

    @Test
    fun `deve retornar Optional vazio quando Champion não for encontrado`() {
        val randomUuid = UUID.randomUUID()
        every {
            cqlSession.execute(
                SimpleStatement.newInstance(
                    "SELECT * from champion where id = ?",
                    randomUuid
                )
            ).one()
        } returns null
        val result = championRepositoryImpl.findById(randomUuid)
        result.isEmpty shouldBe true
    }

    @Test
    fun `deve retornar Champion após tentativa de persistência`() {
        val persisteChampion = ChampionEntity(
            id = UUID.randomUUID(),
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ChampionRole.ASSASSIN,
            difficulty = ChampionDifficulty.MODERATE
        )
        championRepositoryImpl.save(persisteChampion) shouldBe championEntityToModel(persisteChampion)
    }

    @Test
    fun `deve retornar Champion existente após tentativa de atualização`() {
        val updatedChampion = champion.copy(name = "Master Yi")
        val updateResult = championRepositoryImpl.update(updatedChampion)
        updateResult shouldBe championEntityToModel(champion)
    }

    @Test
    fun `deve lançar exceção ao tentar atualizar Champion inexistente`() {
        val updatedChampion = ChampionEntity(
            id = UUID.randomUUID(),
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ChampionRole.ASSASSIN,
            difficulty = ChampionDifficulty.MODERATE
        )
        shouldThrow<RuntimeException> {
            championRepositoryImpl.update(updatedChampion)
        }
    }

    @Test
    fun `deve tentar deletar Champion`() {
        championRepositoryImpl.deleteById(champion.id!!)
    }
}