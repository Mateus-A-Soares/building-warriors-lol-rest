package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.util.HttpHostResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ChampionControllerCreateTests {

    val request = HttpRequest.POST(
        "/api/v1/champions", CreateChampionRequest(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = "MAGE",
            difficulty = "MODERATE"
        )
    )

    @Mock
    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    @Mock
    private val mockedHttpHostResolver: HttpHostResolver = Mockito.mock(HttpHostResolver::class.java)


    @Test
    fun `deve cadastrar champion`() {
        val controller = ChampionController(service = mockedService, httpHostResolver = mockedHttpHostResolver)
        `when`(mockedHttpHostResolver.resolve(request))
            .thenReturn("http://www.ritogomes:8080")
        lateinit var champion: Champion
        val response = request.apply {
            champion = body.get().toModel()
            `when`(mockedService.saveChampion(any(Champion::class.java)))
                .thenReturn(ChampionCreatedResponse(champion.apply { id = 1 }))
        }.run {
            controller.createChampion(this, body.get())
        }

        with(response) {
            assertEquals(HttpStatus.CREATED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            assertEquals(
                "${mockedHttpHostResolver.resolve(request)}/api/v1/champions/${champion.id!!}",
                header("location")
            )
            with(body()!!) {
                assertEquals(champion.id, id)
                assertEquals(champion.name, name)
                assertEquals(champion.shortDescription, shortDescription)
                assertEquals(champion.role, role)
                assertEquals(champion.difficulty, difficulty)
            }
        }
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        `when`(mockedService.saveChampion(any(Champion::class.java)))
            .thenThrow(RuntimeException())
        val controller = ChampionController(mockedService, mockedHttpHostResolver)
        val exception =
            controller.createChampion(httpRequest = request, championRequest = request.body.get())
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, exception.status.code)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}