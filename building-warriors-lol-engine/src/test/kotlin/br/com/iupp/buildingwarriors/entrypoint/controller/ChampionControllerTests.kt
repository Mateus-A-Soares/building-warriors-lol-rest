package br.com.iupp.buildingwarriors.entrypoint.controller

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToResponse
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty.*
import br.com.iupp.buildingwarriors.core.model.ChampionRole.*
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class ChampionControllerTests : AnnotationSpec() {

    private val mockedService = mockk<ChampionServicePort>(relaxed = true)
    private val championController = ChampionController(mockedService)
    private val championList = listOf(
        Champion(
            id = UUID.randomUUID(),
            name = "Rammus",
            shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
            role = TANK,
            difficulty = MODERATE
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Morgana",
            shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
            role = SUPPORT,
            difficulty = LOW
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Ashe",
            shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
            role = MARKSMAN,
            difficulty = MODERATE
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = MAGE,
            difficulty = MODERATE
        ), Champion(
            id = UUID.randomUUID(),
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ASSASSIN,
            difficulty = MODERATE
        ), Champion(
            id = UUID.randomUUID(),
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = FIGHTER,
            difficulty = HIGH
        )
    )

    @Test
    private fun `deveria retornar todos campeões cadastrados`() {
        val responseList = championList.map(::championToResponse)
        every { mockedService.findAll() } returns responseList

        championController.getAll().run {

            status shouldBe HttpStatus.OK
            body() shouldBe responseList
        }
        verify { mockedService.findAll() }
    }

    @Test
    private fun `deveria retornar campeao cadastrado`() {
        every { mockedService.findById(championList[1].id!!) } returns Optional.of(championList[1].let(::championToResponse))

        championController.getById(championList[1].id.toString()).run {

            status shouldBe HttpStatus.OK
            body() shouldBe championList[1].let(::championToResponse)
        }
        verify { mockedService.findById(championList[1].id!!) }
    }

    @Test
    private fun `deveria retornar campeao not found para campeão não encontrado pelo service`() {
        val randomUuid = UUID.randomUUID()
        every { mockedService.findById(randomUuid) } returns Optional.empty()

        championController.getById(randomUuid.toString()).run {

            status shouldBe HttpStatus.NOT_FOUND
            body.isEmpty shouldBe true
        }
        verify { mockedService.findById(randomUuid) }
    }
}