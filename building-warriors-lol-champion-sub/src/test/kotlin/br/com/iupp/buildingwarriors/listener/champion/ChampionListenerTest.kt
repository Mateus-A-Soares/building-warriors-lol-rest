package br.com.iupp.buildingwarriors.listener.champion

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championEventToModel
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.listener.ChampionListener
import br.com.iupp.buildingwarriors.entrypoint.listener.request.ChampionEvent
import br.com.iupp.buildingwarriors.entrypoint.listener.request.ChampionRequest
import br.com.iupp.buildingwarriors.entrypoint.listener.request.Operation.*
import io.kotest.core.spec.style.AnnotationSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class ChampionListenerTest : AnnotationSpec() {

    private val mockedService = mockk<ChampionServicePort>()

    private val listener = ChampionListener(mockedService)

//    @Test
//    fun `deve encontrar todos champions cadastrados`() {
//        val champions: List<ChampionResponse> = listOf(
//            ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Rammus",
//                shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
//                role = ChampionRole.TANK,
//                difficulty = ChampionDifficulty.MODERATE
//            ),
//            ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Morgana",
//                shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
//                role = ChampionRole.SUPPORT,
//                difficulty = ChampionDifficulty.LOW
//            ),
//            ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Ashe",
//                shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
//                role = ChampionRole.MARKSMAN,
//                difficulty = ChampionDifficulty.MODERATE
//            ),
//            ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Ahri",
//                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
//                role = ChampionRole.MAGE,
//                difficulty = ChampionDifficulty.MODERATE
//            ), ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Master Yi",
//                shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
//                role = ChampionRole.ASSASSIN,
//                difficulty = ChampionDifficulty.MODERATE
//            ), ChampionResponse(
//                id = UUID.randomUUID().toString(),
//                name = "Riven",
//                shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
//                role = ChampionRole.FIGHTER,
//                difficulty = ChampionDifficulty.HIGH
//            )
//        )
//        every { mockedService.getAllChampions() } returns champions
//
//        val response = controller.getAllChampions()
//
//        with(response) {
//            status.code shouldBe HttpStatus.OK.code
//            body() shouldBe champions
//        }
//    }
//
//    @Test
//    fun `deve encontrar champion existente`() {
//        val championId = UUID.randomUUID()
//        val championResponse = ChampionResponse(
//            id = championId.toString(),
//            name = "Ahri",
//            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
//            role = ChampionRole.MAGE,
//            difficulty = ChampionDifficulty.MODERATE
//        )
//        every {
//            mockedService.getChampion(championId.toString())
//        } returns Optional.of(championResponse)
//
//        val response = listener.getChampion(championId.toString())
//
//        with(response) {
//            status.code shouldBe HttpStatus.OK.code
//            val body = body()
//            body shouldNotBe null
//            body shouldBe championResponse
//        }
//    }
//
//    @Test
//    fun `deve encontrar champion existente`() {
//        val randomUUID = UUID.randomUUID()
//        every { mockedService.getChampion(randomUUID.toString()) } returns Optional.empty()
//
//        val response = listener.getChampion(randomUUID.toString())
//        response.status.code shouldBe HttpStatus.NOT_FOUND.code
//    }

    @Test
    fun `deve aceitar operação de cadastrado`() {
        val request = ChampionRequest(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = "MAGE",
            difficulty = "MODERATE"
        )
        val championEvent = ChampionEvent(operation = CREATE, championRequest = request)
        every { mockedService.saveChampion(championEventToModel(championEvent.championRequest)) } returns Unit

        listener.getMessage(championEvent)

        verify { mockedService.saveChampion(championEventToModel(championEvent.championRequest)) }
    }

    @Test
    fun `deve aceitar operação de atualização`() {
        val request = ChampionRequest(
            id = UUID.randomUUID().toString(), name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = "MAGE",
            difficulty = "MODERATE"
        )
        val championEvent = ChampionEvent(operation = UPDATE, championRequest = request)
        every { mockedService.updateChampion(championEventToModel(championEvent.championRequest)) } returns Unit

        listener.getMessage(championEvent)

        verify { mockedService.updateChampion(championEventToModel(championEvent.championRequest)) }
    }

    @Test
    fun `deve aceitar operação de exclusão`() {
        val request = ChampionRequest(id = UUID.randomUUID().toString())
        val championEvent = ChampionEvent(operation = DELETE, championRequest = request)
        every { mockedService.deleteChampion(championEventToModel(championEvent.championRequest)) } returns Unit

        listener.getMessage(championEvent)

        verify { mockedService.deleteChampion(championEventToModel(championEvent.championRequest)) }
    }
}