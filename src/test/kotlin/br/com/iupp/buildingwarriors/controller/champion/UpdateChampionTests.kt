package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.controller.champion.response.ConstraintErrorDto
import br.com.iupp.buildingwarriors.controller.champion.response.ConstraintErrorDto.Embedded.ErrorMessage
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
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.inject.Inject

@MicronautTest(transactional = false, environments = ["test"])
class UpdateChampionTests(
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
    fun `deve atualizar champion cadastrado com dados validos`() {
        val updateChampionRequest = UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        val request = HttpRequest.PUT("/api/v1/champions/${champion.id}", updateChampionRequest)

        val response = championsClient.toBlocking()
            .exchange(request, ChampionDetailsResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.ACCEPTED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            val championUpdated: Champion = championRepository.findById(champion.id!!).get()
            assertNotNull(championUpdated)
            assertEquals(
                "${httpHostResolver.resolve(request)}/api/v1/champions/${championUpdated.id}",
                header("location")
            )
            with(body()!!) {
                assertEquals(updateChampionRequest.name, name, championUpdated.name)
                assertEquals(
                    updateChampionRequest.shortDescription,
                    shortDescription,
                    championUpdated.shortDescription
                )
                assertEquals(
                    updateChampionRequest.role!!.toUpperCase(),
                    role.toString(),
                    championUpdated.role.toString()
                )
                assertEquals(
                    updateChampionRequest.difficulty!!.toUpperCase(),
                    difficulty.toString(),
                    championUpdated.difficulty.toString()
                )
            }
        }
    }

    @Test
    fun `nao deve atualizar dados enviados vazios ou nulos`() {
        val updateChampionRequest = UpdateChampionRequest()
        val request = HttpRequest.PUT("/api/v1/champions/${champion.id}", updateChampionRequest)

        val response = championsClient.toBlocking()
            .exchange(request, ChampionDetailsResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.ACCEPTED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            assertEquals(
                "${httpHostResolver.resolve(request)}/api/v1/champions/${champion.id}",
                header("location")
            )
            with(body()!!) {
                assertEquals(champion.name, name)
                assertEquals(champion.shortDescription, shortDescription)
                assertEquals(champion.role, role)
                assertEquals(champion.difficulty, difficulty)
            }
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        val request = HttpRequest.PUT("/api/v1/champions/2", UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        ))

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionDetailsResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND.code, exception.status.code)
    }

    @Test
    fun `deve retornar erros de validacao para dados invalido`() {
        val updateChampionRequest = UpdateChampionRequest(
            role = "WARRIOR",
            difficulty = "EASY"
        )
        val request = HttpRequest.PUT("/api/v1/champions/1", updateChampionRequest)

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionDetailsResponse::class.java)
        }

        val optionalBody = exception.response.getBody(ConstraintErrorDto::class.java)
        assertTrue(optionalBody.isPresent)
        assertNotNull(optionalBody.get().errors)
        with(optionalBody.get().errors) {
            assertThat(
                this, containsInAnyOrder(
                    ErrorMessage(message = "updateChampionRequest.role: Deve ser um dos seguintes valores: ASSASSIN, FIGHTER, MAGE, MARKSMAN, SUPPORT, TANK"),
                    ErrorMessage(message = "updateChampionRequest.difficulty: Deve ser um dos seguintes valores: LOW, MODERATE, HIGH")
                )
            )
        }
    }

    @Test
    fun `deve retornar entidade nao processavel para champion com nome ja cadastrado`() {
        championRepository.save(
            Champion(
                name = "Riven",
                shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
                role = ChampionRole.FIGHTER,
                difficulty = ChampionDifficulty.HIGH
            )
        )

        val request = HttpRequest.PUT(
            "/api/v1/champions/1", UpdateChampionRequest(name = "Riven")
        )

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionDetailsResponse::class.java)
        }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.code, exception.status.code)
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        val serviceMock = Mockito.mock(ChampionService::class.java)
        `when`(serviceMock.updateChampion(any(Long::class.java), any(UpdateChampionRequest::class.java)))
            .thenThrow(RuntimeException())
        val controller = ChampionController(serviceMock, httpHostResolver)
        val requestBody = UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        val httpRequest = HttpRequest.PUT("/api/v1/champions/1", requestBody)
        val exception =
            controller.updateChampion(httpRequest = httpRequest, id = 1, updateChampionRequest = requestBody,)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, exception.status.code)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
}