package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito

@MicronautTest
class ChampionControllerGetAllTests {

    private var champions: List<ChampionResponse> = listOf(
        ChampionResponse(
            id = 1,
            name = "Rammus",
            shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
            role = ChampionRole.TANK,
            difficulty = ChampionDifficulty.MODERATE
        ),
        ChampionResponse(
            id = 2,
            name = "Morgana",
            shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
            role = ChampionRole.SUPPORT,
            difficulty = ChampionDifficulty.LOW
        ),
        ChampionResponse(
            id = 3,
            name = "Ashe",
            shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
            role = ChampionRole.MARKSMAN,
            difficulty = ChampionDifficulty.MODERATE
        ),
        ChampionResponse(
            id = 5,
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ), ChampionResponse(
            id = 6,
            name = "Master Yi",
            shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
            role = ChampionRole.ASSASSIN,
            difficulty = ChampionDifficulty.MODERATE
        ), ChampionResponse(
            id = 7,
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = ChampionRole.FIGHTER,
            difficulty = ChampionDifficulty.HIGH
        )
    )

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        Mockito.`when`(mockedService.getAllChampions())
            .thenReturn(champions)
        val controller = ChampionController(service = mockedService)

        val response = controller.getAllChampions()

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            assertThat(body(), containsInAnyOrder(*champions.toTypedArray()))
        }
    }
}