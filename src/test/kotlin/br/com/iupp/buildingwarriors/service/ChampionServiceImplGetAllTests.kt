package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito

class ChampionServiceImplGetAllTests {
    private var champions: List<Champion> = listOf(
        Champion(
            name = "Rammus",
            shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
            role = ChampionRole.TANK,
            difficulty = ChampionDifficulty.MODERATE
        ),
        Champion(
            name = "Morgana",
            shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
            role = ChampionRole.SUPPORT,
            difficulty = ChampionDifficulty.LOW
        ),
        Champion(
            name = "Ashe",
            shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
            role = ChampionRole.MARKSMAN,
            difficulty = ChampionDifficulty.MODERATE
        ),
        Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ), Champion(
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ChampionRole.ASSASSIN,
            difficulty = ChampionDifficulty.MODERATE
        ), Champion(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = ChampionRole.FIGHTER,
            difficulty = ChampionDifficulty.HIGH
        )
    )

    @Mock
    private val mockedRepository: ChampionRepository = Mockito.mock(ChampionRepository::class.java)

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        Mockito.`when`(mockedRepository.findAll())
            .thenReturn(champions)
        val service = ChampionServiceImpl(mockedRepository)

        val functionReturn = service.getAllChampions()

        val championsDetailsResponse = champions.map { ChampionDetailsResponse(it) }
        MatcherAssert.assertThat(functionReturn, Matchers.containsInAnyOrder(*championsDetailsResponse.toTypedArray()))
    }
}