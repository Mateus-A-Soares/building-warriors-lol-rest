package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(transactional = false, environments = ["test"])
class CreateChampionTests(@Inject val championRepository: ChampionRepository) {

    @Inject
    @field:Client("/", errorType = String::class)
    lateinit var championsClient: HttpClient

    @BeforeEach
    fun setup() {
        championRepository.deleteAll()
    }

    @Test
    fun `deve cadastrar champion valido`() {
        val createChampionRequestBody = CreateChampionRequest(
            name = "AhasdasdriI",
            shortDescription = "Com uma conexxao",
            role = "MAGE",
            difficulty = "MODERATE"
        )

        val request = HttpRequest.POST(
            "/api/v1/champions", createChampionRequestBody
        )

        val response = championsClient.toBlocking()
            .exchange(request, ChampionCreatedResponse::class.java)

        with(response) {
            assertEquals(HttpStatus.OK.code, status.code)
            val body = body()
            assertNotNull(body)
            assertNotNull(body!!.id)
            val championCreated: Champion? = championRepository.findById(body.id).orElse(null)
            assertNotNull(championCreated)
            with(body()!!) {
                assertEquals(createChampionRequestBody.name, name, championCreated!!.name)
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
}
