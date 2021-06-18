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
        every { mockedService.getAllChampions() } returns champions

        val response = controller.getAllChampions()

        with(response) {
            HttpStatus.OK.code shouldBe  status.code
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
        val championId = 1L
        request.body.get().apply {
            every { mockedService.saveChampion(this@apply) } returns
                    ChampionResponse(
                        id = championId,
                        name = name!!,
                        shortDescription = shortDescription!!,
                        role = ChampionRole.valueOf(role!!),
                        difficulty = ChampionDifficulty.valueOf(difficulty!!)
                    )
        }
        val response = controller.createChampion(request, createRequest)

        with(response) {
            CREATED.code shouldBe  status.code
            val body = body()
            body!!.id shouldNotBe null
            "${request.path}/${body.id}" shouldBe header("location")
            with(body()!!) {
                championId shouldBe id
                createRequest.name shouldBe name
                createRequest.shortDescription shouldBe shortDescription
                createRequest.role!!.toUpperCase() shouldBe role.toString()
                createRequest.difficulty!!.toUpperCase() shouldBe difficulty.toString()
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
        every {
            mockedService.getChampion(championResponse.id!!)
        } returns Optional.of(championResponse)

        val response = controller.getChampion(championResponse.id!!)

        with(response) {
            HttpStatus.OK.code shouldBe status.code
            val body = body()
            body shouldNotBe null
            championResponse shouldBe body
        }
    }

    @Test
    fun `deve retornar status 404 para id inexistente`() {
        every { mockedService.getChampion(2) } returns Optional.empty()

        val response = controller.getChampion(2)
        HttpStatus.NOT_FOUND.code shouldBe response.status.code
    }


    @Test
    fun `deve deletar champion existente`() {
        val id = 1L
        every { mockedService.deleteChampion(id) } returns Unit
        val response = controller.deleteChampion(id)
        HttpStatus.NO_CONTENT.code shouldBe response.status.code
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
        every {
            mockedService.updateChampion(champion.id!!, updateRequest)
        } returns Optional.of(ChampionResponse(champion = champion))

        val response = controller.updateChampion(
            httpRequest = HttpRequest.PUT("/api/v1/champions/${champion.id}", updateRequest),
            championRequest = updateRequest,
            id = champion.id!!
        )

        with(response) {
            HttpStatus.ACCEPTED.code shouldBe status.code
            body() shouldNotBe null
            ChampionResponse(champion) shouldBe body()
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
        every {
            mockedService.updateChampion(2, updateRequest)} returns Optional.empty()

            val response =
                controller.updateChampion(
                    id = 2,
                    httpRequest = HttpRequest.PUT("/api/v1/champions/${2}", updateRequest),
                    championRequest = updateRequest
                )

            HttpStatus.NOT_FOUND.code shouldBe response.status.code
        }
    }