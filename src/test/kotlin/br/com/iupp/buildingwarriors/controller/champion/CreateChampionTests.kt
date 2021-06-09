package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.CreateChampionTests.ConstraintErrorDto.Embedded.ErrorMessage
import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
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
class CreateChampionTests(
    @Inject val championRepository: ChampionRepository,
    @Inject val httpHostResolver: HttpHostResolver
) {

    @Inject
    @field:Client("/", errorType = ConstraintErrorDto::class)
    lateinit var championsClient: HttpClient

    @BeforeEach
    fun setup() {
        championRepository.deleteAll()
    }

    @Test
    fun `deve cadastrar champion valido`() {
        val createChampionRequestBody = CreateChampionRequest(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = "MAGE",
            difficulty = "MODERATE"
        )
        val request = HttpRequest.POST(
            "/api/v1/champions", createChampionRequestBody
        )

        val response = championsClient.toBlocking()
            .exchange(request, ChampionCreatedResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.CREATED.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            val championCreated: Champion? = championRepository.findById(body.id!!).orElse(null)
            assertNotNull(championCreated)
            assertEquals( "${httpHostResolver.resolve(request)}/api/v1/champions/${championCreated!!.id}", header("location"))
            with(body()!!) {
                assertEquals(createChampionRequestBody.name, name, championCreated.name)
                assertEquals(
                    createChampionRequestBody.shortDescription,
                    shortDescription,
                    championCreated.shortDescription
                )
                assertEquals(
                    createChampionRequestBody.role!!.toUpperCase(),
                    role.toString(),
                    championCreated.role.toString()
                )
                assertEquals(
                    createChampionRequestBody.difficulty!!.toUpperCase(),
                    difficulty.toString(),
                    championCreated.difficulty.toString()
                )
            }
        }
    }

    @Test
    fun `deve retornar erros de validacao para champion invalido`() {
        val createChampionRequestBody = CreateChampionRequest(
            name = "",
            shortDescription = "",
            role = "WARRIOR",
            difficulty = "EASY"
        )
        val request = HttpRequest.POST(
            "/api/v1/champions", createChampionRequestBody
        )

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionCreatedResponse::class.java)
        }

        val optionalBody = exception.response.getBody(ConstraintErrorDto::class.java)
        assertTrue(optionalBody.isPresent)
        assertNotNull(optionalBody.get().errors)
        with(optionalBody.get().errors) {
            assertThat(
                this, containsInAnyOrder(
                    ErrorMessage(message = "championRequest.role: Deve ser um dos seguintes valores: ASSASSIN, FIGHTER, MAGE, MARKSMAN, SUPPORT, TANK"),
                    ErrorMessage(message = "championRequest.difficulty: Deve ser um dos seguintes valores: LOW, MODERATE, HIGH"),
                    ErrorMessage(message = "championRequest.shortDescription: must not be blank"),
                    ErrorMessage(message = "championRequest.name: must not be blank")
                )
            )
        }
    }

    @Test
    fun `deve retornar erro de validacao para champion com nome ja cadastrado`() {
        championRepository.save(
            Champion(
                name = "Ahri",
                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
                role = ChampionRole.MAGE,
                difficulty = ChampionDifficulty.MODERATE
            )
        )

        val request = HttpRequest.POST(
            "/api/v1/champions", CreateChampionRequest(
                name = "Ahri",
                shortDescription = "A raposa de nove caudas",
                role = "ASSASSIN",
                difficulty = "HIGH"
            )
        )

        val exception = assertThrows<HttpClientResponseException> {
            championsClient.toBlocking()
                .exchange(request, ChampionCreatedResponse::class.java)
        }

        val optionalBody = exception.response.getBody(ConstraintErrorDto::class.java)
        assertTrue(optionalBody.isPresent)
        assertEquals("championRequest.name: O nome deve ser único", optionalBody.get().message)
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        val serviceMock = Mockito.mock(ChampionService::class.java)
        `when`(serviceMock.saveChampion(any(Champion::class.java)))
            .thenThrow(RuntimeException())
        val controller = ChampionController(serviceMock, httpHostResolver)
        val requestBody = CreateChampionRequest(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE.toString(),
            difficulty = ChampionDifficulty.MODERATE.toString()
        )
        val httpRequest = HttpRequest.POST("/api/v1/champions", requestBody)
        val exception =
            controller.createChampion(httpRequest = httpRequest, championRequest = requestBody)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.code, exception.status.code)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

    private data class ConstraintErrorDto(
        val message: String?,
        val _embedded: Embedded?
    ) {
        val errors: List<ErrorMessage?>?
            get() = _embedded?.errors

        data class Embedded(val errors: List<ErrorMessage?>?) {
            data class ErrorMessage(val message: String?)
        }
    }
}