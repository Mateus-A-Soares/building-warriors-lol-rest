package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionControllerTest : AnnotationSpec() {

    private val mockedService = mockk<ChampionService>()

    private val controller = ChampionController(mockedService)

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        val champions: List<ChampionResponse> = listOf(
            ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Rammus",
                shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
                role = ChampionRole.TANK,
                difficulty = ChampionDifficulty.MODERATE
            ),
            ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Morgana",
                shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
                role = ChampionRole.SUPPORT,
                difficulty = ChampionDifficulty.LOW
            ),
            ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Ashe",
                shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
                role = ChampionRole.MARKSMAN,
                difficulty = ChampionDifficulty.MODERATE
            ),
            ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Ahri",
                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
                role = ChampionRole.MAGE,
                difficulty = ChampionDifficulty.MODERATE
            ), ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Master Yi",
                shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
                role = ChampionRole.ASSASSIN,
                difficulty = ChampionDifficulty.MODERATE
            ), ChampionResponse(
                id = UUID.randomUUID().toString(),
                name = "Riven",
                shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
                role = ChampionRole.FIGHTER,
                difficulty = ChampionDifficulty.HIGH
            )
        )
        every { mockedService.getAllChampions() } returns champions

        val response = controller.getAllChampions()

        with(response) {
            status.code shouldBe HttpStatus.OK.code
            body() shouldBe champions
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
        val championId = UUID.randomUUID()
        request.body.get().apply {
            every { mockedService.saveChampion(this@apply) } returns
                    ChampionResponse(
                        id = championId.toString(),
                        name = name!!,
                        shortDescription = shortDescription!!,
                        role = ChampionRole.valueOf(role!!),
                        difficulty = ChampionDifficulty.valueOf(difficulty!!)
                    )
        }
        val response = controller.createChampion(request, createRequest)

        with(response) {
            status.code shouldBe CREATED.code
            val body = body()!!
            body.id shouldNotBe null
            header("location") shouldBe "${request.path}/${body.id}"
            with(body()!!) {
                id shouldBe championId.toString()
                name shouldBe createRequest.name
                shortDescription shouldBe createRequest.shortDescription
                role.toString() shouldBe createRequest.role!!.toUpperCase()
                difficulty.toString() shouldBe createRequest.difficulty!!.toUpperCase()
            }
        }
    }

    @Test
    fun `deve encontrar champion existente`() {
        val championId = UUID.randomUUID()
        val championResponse = ChampionResponse(
            id = championId.toString(),
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        )
        every {
            mockedService.getChampion(championId.toString())
        } returns Optional.of(championResponse)

        val response = controller.getChampion(championId.toString())

        with(response) {
            status.code shouldBe HttpStatus.OK.code
            val body = body()
            body shouldNotBe null
            body shouldBe championResponse
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        val randomUUID = UUID.randomUUID()
        every { mockedService.getChampion(randomUUID.toString()) } returns Optional.empty()

        val response = controller.getChampion(randomUUID.toString())
        response.status.code shouldBe HttpStatus.NOT_FOUND.code
    }


    @Test
    fun `deve deletar champion existente`() {
        val championId = UUID.randomUUID()
        every { mockedService.deleteChampion(championId.toString()) } returns Unit
        val response = controller.deleteChampion(championId.toString())
        response.status.code shouldBe HttpStatus.NO_CONTENT.code
    }

    @Test
    fun `deve atualizar champion cadastrado`() {
        val champion = Champion(
            id = UUID.randomUUID(),
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = ChampionRole.FIGHTER,
            difficulty = ChampionDifficulty.HIGH
        )
        val updateRequest = champion.run {
            ChampionRequest(
                name = name,
                shortDescription = shortDescription,
                role = role.toString(),
                difficulty = difficulty.toString()
            )
        }
        every {
            mockedService.updateChampion(champion.id.toString(), updateRequest)
        } returns Optional.of(ChampionResponse(champion = champion))

        val response = controller.updateChampion(
            httpRequest = HttpRequest.PUT("/api/v1/champions/${champion.id.toString()}", updateRequest),
            championRequest = updateRequest,
            id = champion.id.toString()
        )

        with(response) {
            status.code shouldBe HttpStatus.ACCEPTED.code
            body() shouldNotBe null
            body() shouldBe ChampionResponse(champion)
        }
    }

    @Test
    fun `deve retornar status 404 para update de id inexistente`() {
        val updateRequest = ChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        val controller = ChampionController(mockedService)
        val randomUUID = UUID.randomUUID()
        every {
            mockedService.updateChampion(randomUUID.toString(), updateRequest)
        } returns Optional.empty()

        val response =
            controller.updateChampion(
                id = randomUUID.toString(),
                httpRequest = HttpRequest.PUT("/api/v1/champions/$randomUUID", updateRequest),
                championRequest = updateRequest
            )

        response.status.code shouldBe HttpStatus.NOT_FOUND.code
    }
}