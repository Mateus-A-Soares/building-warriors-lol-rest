package br.com.iupp.buildingwarriors.infrastructure.repository

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper
import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championEntityToModel
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty.*
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionRole.*
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionRepositoryImplTests : AnnotationSpec() {

    private val mockedCqlSession = mockk<CqlSession>(relaxed = true)
    private val championRepositoryImpl = ChampionRepositoryImpl(mockedCqlSession)

    private val championList = listOf(
        ChampionEntity(
            id = UUID.randomUUID(),
            name = "Rammus",
            shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
            role = TANK,
            difficulty = MODERATE
        ),
        ChampionEntity(
            id = UUID.randomUUID(),
            name = "Morgana",
            shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
            role = SUPPORT,
            difficulty = LOW
        ),
        ChampionEntity(

            id = UUID.randomUUID(),
            name = "Ashe",
            shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
            role = MARKSMAN,
            difficulty = MODERATE
        ),
        ChampionEntity(
            id = UUID.randomUUID(),
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = MAGE,
            difficulty = MODERATE
        ), ChampionEntity(
            id = UUID.randomUUID(),
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ASSASSIN,
            difficulty = MODERATE
        ), ChampionEntity(
            id = UUID.randomUUID(),
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = FIGHTER,
            difficulty = HIGH
        )
    )

    @Test
    private fun `deveria retornar todos os registros encontrados pelo CqlSession`() {
        every {
            mockedCqlSession.execute(SimpleStatement.newInstance("SELECT * FROM champion")).all()
        } returns championList.map(this::championEntityToMockedRow)
        championRepositoryImpl.findAll() shouldBe championList.map(ChampionMapper::championEntityToModel)
    }

    @Test
    private fun `deveria retornar o registro encontrado pelo CqlSession`() {
        every {
            mockedCqlSession.execute(
                SimpleStatement.newInstance(
                    "SELECT * from champion where id = ?",
                    championList[1].id
                )
            ).one()
        } returns championEntityToMockedRow(championList[1])
        val result = championRepositoryImpl.findById(championList[1].id!!)
        result.isPresent shouldBe true
        result.get() shouldBe championEntityToModel(championList[1])
    }

    @Test
    private fun `deveria retornar optional vazio quando registro nao e encontrado pelo CqlSession`() {
        val randomUUID = UUID.randomUUID()
        every {
            mockedCqlSession.execute(
                SimpleStatement.newInstance(
                    "SELECT * from champion where id = ?",
                    randomUUID
                )
            ).one()
        } returns null
        val result = championRepositoryImpl.findById(randomUUID)
        result.isEmpty shouldBe true
    }

    private fun championEntityToMockedRow(championEntity: ChampionEntity) = with(championEntity) {
        val mockedRow = mockk<Row>()
        every { mockedRow.getUuid("id") } returns id
        every { mockedRow.getString("name") } returns name
        every { mockedRow.getString("shortDescription") } returns shortDescription
        every { mockedRow.getString("role") } returns role.toString()
        every { mockedRow.getString("difficulty") } returns difficulty.toString()
        mockedRow
    }
}