package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.util.HttpHostResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class ChampionControllerGetTests {

    private val championResponse = ChampionResponse(
        id = 1,
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE,
        difficulty = ChampionDifficulty.MODERATE
    )

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    private val mockedHttpHostResolver: HttpHostResolver = Mockito.mock(HttpHostResolver::class.java)

    @Test
    fun `deve encontrar champion existente`() {
        `when`(mockedService.getChampion(championResponse.id!!))
            .thenReturn(Optional.of(championResponse))
        val controller = ChampionController(service = mockedService, httpHostResolver = mockedHttpHostResolver)

        val response = controller.getChampion(championResponse.id!!)

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            val body = body()
            assertNotNull(body)
            with(body!!) {
                assertEquals(championResponse.id, id)
                assertEquals(championResponse.name, name)
                assertEquals(championResponse.shortDescription, shortDescription)
                assertEquals(championResponse.role, role)
                assertEquals(championResponse.difficulty, difficulty)
            }
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        `when`(mockedService.getChampion(2))
            .thenReturn(Optional.empty())
        val controller = ChampionController(service = mockedService, httpHostResolver = mockedHttpHostResolver)

        val response = controller.getChampion(2)

        assertEquals(HttpStatus.NOT_FOUND.code, response.status.code)
    }

    @Test
    fun `deve retornar HttpResponse com status 500 quando ocorrer exception inesperada`() {
        `when`(mockedService.getChampion(championResponse.id!!))
            .thenThrow(RuntimeException())
        val controller = ChampionController(service = mockedService, httpHostResolver = mockedHttpHostResolver)

        val response = controller.getChampion(championResponse.id!!)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, response.status.code)
    }
}
