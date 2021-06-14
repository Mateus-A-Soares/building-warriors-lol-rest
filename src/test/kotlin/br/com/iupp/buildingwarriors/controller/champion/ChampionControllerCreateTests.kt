package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@MicronautTest
class ChampionControllerCreateTests {

    private val request = HttpRequest.POST(
        "/api/v1/champions", ChampionRequest(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = "MAGE",
            difficulty = "MODERATE"
        )
    )
    private val championId = 1L

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    @Test
    fun `deve cadastrar champion`() {
        val controller = ChampionController(service = mockedService)
        lateinit var createRequest: ChampionRequest
        val response = request.apply {
            createRequest = body.get()
            `when`(mockedService.saveChampion(any(Champion::class.java)))
                .thenReturn(ChampionResponse(createRequest.toModel().apply { id = championId }))
        }.run {
            controller.createChampion(this, body.get())
        }

        with(response) {
            assertEquals(HttpStatus.CREATED.code, status.code)
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

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}