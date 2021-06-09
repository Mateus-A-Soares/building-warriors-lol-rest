package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.controller.champion.response.ConstraintErrorDto
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.inject.Inject

@MicronautTest(transactional = false, environments = ["test"])
class GetChampionTests(
    @Inject val championRepository: ChampionRepository,
    @Inject val httpHostResolver: HttpHostResolver
) {

    private lateinit var champion: Champion

    @Inject
    @field:Client("/", errorType = ConstraintErrorDto::class)
    lateinit var championsClient: HttpClient

    @BeforeEach
    fun setup() {
        championRepository.deleteAll()
        champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        )
        championRepository.save(champion)
    }

    @Test
    fun `deve encontrar champion existente`() {
        val request: HttpRequest<Unit> = HttpRequest.GET("/api/v1/champions/${champion.id}")

        val response = championsClient.toBlocking()
            .exchange(request, ChampionDetailsResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            val body = body()
            assertNotNull(body)
            with(body!!) {
                assertEquals(champion.id, id)
                assertEquals(champion.name, name)
                assertEquals(champion.shortDescription, shortDescription)
                assertEquals(champion.role, role)
                assertEquals(champion.difficulty, difficulty)
            }
        }
    }

    @Test
    fun `deve retornar erro de validacao para id invalido`() {
        val request: HttpRequest<Unit> = HttpRequest.GET("/api/v1/champions/0")

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionDetailsResponse::class.java)
        }

        val optionalBody = exception.response.getBody(ConstraintErrorDto::class.java)
        assertTrue(optionalBody.isPresent)
        assertEquals("id: Deve ser um numero positivo", optionalBody.get().message)
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        val request: HttpRequest<Unit> = HttpRequest.GET("/api/v1/champions/2")

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionDetailsResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND.code, exception.status.code)
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        val serviceMock = Mockito.mock(ChampionService::class.java)
        `when`(serviceMock.getChampion(any(Long::class.java)))
            .thenThrow(RuntimeException())
        val controller = ChampionController(serviceMock, httpHostResolver)

        val response = controller.getChampion(champion.id!!)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, response.status.code)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}