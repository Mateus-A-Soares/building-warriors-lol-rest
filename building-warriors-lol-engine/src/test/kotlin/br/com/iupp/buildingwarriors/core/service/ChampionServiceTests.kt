package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToResponse
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionServiceTests : AnnotationSpec() {

    private val mockedRepository = mockk<ChampionRepositoryPort>()
    private val service = ChampionServiceImpl(mockedRepository)
    private val championList = listOf(
        Champion(
            id = UUID.randomUUID(),
            name = "Rammus",
            shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
            role = ChampionRole.TANK,
            difficulty = ChampionDifficulty.MODERATE
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Morgana",
            shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
            role = ChampionRole.SUPPORT,
            difficulty = ChampionDifficulty.LOW
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Ashe",
            shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
            role = ChampionRole.MARKSMAN,
            difficulty = ChampionDifficulty.MODERATE
        ),
        Champion(
            id = UUID.randomUUID(),
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ), Champion(
            id = UUID.randomUUID(),
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ChampionRole.ASSASSIN,
            difficulty = ChampionDifficulty.MODERATE
        ), Champion(
            id = UUID.randomUUID(),
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = ChampionRole.FIGHTER,
            difficulty = ChampionDifficulty.HIGH
        )
    )

    @Test
    private fun `Deveria retornar todos campeoes encontrados pelo repositorio`() {
        every {mockedRepository.findAll()} returns championList
        service.findAll() shouldBe championList.map(::championToResponse)
    }

    @Test
    private fun `Deveria retornar campeao encontrado pelo repositorio`() {
        every {mockedRepository.findById(championList[1].id!!)} returns Optional.of(championList[1])
        service.findById(championList[1].id!!) shouldBe Optional.of(championList[1].let(::championToResponse))
    }

    @Test
    private fun `Deveria retornar todos campeões encontrados`() {
        val randomUuid = UUID.randomUUID()
        every {mockedRepository.findById(randomUuid)} returns Optional.empty()
        service.findById(randomUuid).isEmpty shouldBe true
    }
}