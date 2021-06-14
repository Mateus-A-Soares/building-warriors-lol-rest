package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.util.HttpHostResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class ChampionControllerUpdateTests {

    private val championId = 1L
    private var updateRequest = HttpRequest.POST(
        "/api/v1/champions", UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
    )
    private val updateResponse = with(updateRequest.body.get()) {
        ChampionDetailsResponse(
            id = championId,
            name = name!!,
            shortDescription = shortDescription!!,
            difficulty = ChampionDifficulty.HIGH,
            role = ChampionRole.FIGHTER
        )
    }

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    private val mockedHttpHostResolver: HttpHostResolver = Mockito.mock(HttpHostResolver::class.java)


    @Test
    fun `deve atualizar champion cadastrado`() {
        val controller = ChampionController(mockedService, mockedHttpHostResolver)
        val updateRequestBody = updateRequest.body.get()
        `when`(mockedService.updateChampion(championId, updateRequestBody))
            .thenReturn(Optional.of(updateResponse))
        `when`(mockedHttpHostResolver.resolve(updateRequest))
            .thenReturn("http://www.ritogomes:8080")

        val response = controller.updateChampion(
            httpRequest = updateRequest,
            updateChampionRequest = updateRequestBody,
            id = championId
        )

        with(response) {
            assertEquals(HttpStatus.ACCEPTED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            assertEquals(
                "${mockedHttpHostResolver.resolve(updateRequest)}/api/v1/champions/${championId}",
                header("location")
            )
            with(body()!!) {
                assertEquals(updateRequestBody.name, name)
                assertEquals(updateRequestBody.shortDescription, shortDescription)
                assertEquals(updateRequestBody.role!!.toUpperCase(), role.toString())
                assertEquals(updateRequestBody.difficulty!!.toUpperCase(), difficulty.toString())
            }
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        val controller = ChampionController(mockedService, mockedHttpHostResolver)
        val updateRequestBody = updateRequest.body.get()
        `when`(mockedService.updateChampion(2, updateRequestBody))
            .thenReturn(Optional.empty())

        val response =
            controller.updateChampion(id = 2, httpRequest = updateRequest, updateChampionRequest = updateRequestBody)

        assertEquals(HttpStatus.NOT_FOUND.code, response.status.code)
    }

    @Test
    fun `deve retornar entidade nao processavel para champion com nome ja cadastrado`() {
        val controller = ChampionController(mockedService, mockedHttpHostResolver)
        val updateRequestBody = updateRequest.body.get()
        `when`(mockedService.updateChampion(championId, updateRequestBody))
            .thenAnswer { throw UniqueFieldAlreadyExistsException(entity = "champion", field = "name") }

        val response =
            controller.updateChampion(
                id = championId,
                httpRequest = updateRequest,
                updateChampionRequest = updateRequestBody
            )

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, response.status.code)
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        val requestBody = updateRequest.body.get()
        `when`(mockedService.updateChampion(championId, requestBody))
            .thenThrow(RuntimeException())
        val controller = ChampionController(mockedService, mockedHttpHostResolver)
        val exception =
            controller.updateChampion(httpRequest = updateRequest, id = championId, updateChampionRequest = requestBody)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, exception.status.code)
    }
}