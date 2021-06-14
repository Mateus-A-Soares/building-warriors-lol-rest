package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

@MicronautTest
class ChampionControllerTests {

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)
    private val controller = ChampionController(service = mockedService)

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        val champions: List<ChampionResponse> = listOf(
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
        Mockito.`when`(mockedService.getAllChampions())
            .thenReturn(champions)

        val response = controller.getAllChampions()

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            assertThat(body(), containsInAnyOrder(*champions.toTypedArray()))
        }
    }

    @Test
    fun `deve cadastrar champion`() {
        val request = HttpRequest.POST(
            "/api/v1/champions", ChampionRequest(
                name = "Ahri",
                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
                role = "MAGE",
                difficulty = "MODERATE"
            )
        )
        val createRequest: ChampionRequest = request.body.get()
        val championId = 1L
        request.body.get().apply {
            Mockito.`when`(mockedService.saveChampion(this))
                .thenReturn(
                    ChampionResponse(
                            id = championId,
                            name = name!!,
                            shortDescription = shortDescription!!,
                            role = ChampionRole.valueOf(role!!),
                            difficulty = ChampionDifficulty.valueOf(difficulty!!)
                    )
                )
        }
        val response = controller.createChampion(request, createRequest)

        with(response) {
            assertEquals(io.micronaut.http.HttpStatus.CREATED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            assertEquals(
                "${request.path}/${body.id}",
                header("location")
            )
            with(body()!!) {
                assertEquals(championId, id)
                assertEquals(createRequest.name, name)
                assertEquals(createRequest.shortDescription, shortDescription)
                assertEquals(createRequest.role!!.toUpperCase(), role.toString())
                assertEquals(createRequest.difficulty!!.toUpperCase(), difficulty.toString())
            }
        }
    }

    @Test
    fun `deve encontrar champion existente`() {
        val championResponse = ChampionResponse(
            id = 1,
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        )
        Mockito.`when`(mockedService.getChampion(championResponse.id!!))
            .thenReturn(Optional.of(championResponse))

        val response = controller.getChampion(championResponse.id!!)

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            val body = body()
            assertNotNull(body)
            assertEquals(championResponse, body)
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        Mockito.`when`(mockedService.getChampion(2))
            .thenReturn(Optional.empty())

        val response = controller.getChampion(2)

        assertEquals(HttpStatus.NOT_FOUND.code, response.status.code)
    }

    @Test
    fun `deve deletar champion existente`() {
        val response = controller.deleteChampion(1L)
        assertEquals(HttpStatus.NO_CONTENT.code, response.status.code)
    }

    @Test
    fun `deve atualizar champion cadastrado`() {
        var champion = Champion(
            id = 1L,
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = ChampionRole.FIGHTER,
            difficulty = ChampionDifficulty.HIGH
        )
        val updateRequest = ChampionRequest(
            name = champion.name,
            shortDescription = champion.shortDescription,
            role = champion.role.toString(),
            difficulty = champion.difficulty.toString()
        )
        Mockito.`when`(
            mockedService.updateChampion(champion.id!!, updateRequest)
        ).thenReturn(Optional.of(ChampionResponse(champion = champion)))

        val response = controller.updateChampion(
            httpRequest = HttpRequest.PUT("/api/v1/champions/${champion.id}", updateRequest),
            championRequest = updateRequest,
            id = champion.id!!
        )

        with(response) {
            assertEquals(HttpStatus.ACCEPTED.code, status.code)
            assertNotNull(body())
            assertEquals(ChampionResponse(champion), body())
        }
    }

    @Test
    fun `deve retornar status 404 para update de id inexistente`() {
        var updateRequest = ChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        val controller = ChampionController(mockedService)
        Mockito.`when`(mockedService.updateChampion(2, updateRequest))
            .thenReturn(Optional.empty())

        val response =
            controller.updateChampion(
                id = 2,
                httpRequest = HttpRequest.PUT("/api/v1/champions/${2}", updateRequest),
                championRequest = updateRequest
            )

        assertEquals(HttpStatus.NOT_FOUND.code, response.status.code)
    }
}