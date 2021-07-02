package br.com.iupp.buildingwarriors.entrypoint.controller

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championRequestToChampion
import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToChampionResponse
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.request.ChampionRequest
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class ChampionControllerTests : AnnotationSpec() {

    private val mockedService = mockk<ChampionServicePort>(relaxed = true)
    private val championController = ChampionController(mockedService)
    private val request = ChampionRequest(
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE.toString(),
        difficulty = ChampionDifficulty.MODERATE.toString()
    )
    private val champion = championRequestToChampion(request)
    private val response = championToChampionResponse(champion)

    @Test
    private fun `deveria tentar cadastrar campeão`() {
        every { mockedService.createRequest(champion) } returns response

        championController.createChampion(request).run {

            status shouldBe HttpStatus.OK
            body() shouldBe response
        }
        verify { mockedService.createRequest(champion) }
    }

    @Test
    private fun `deveria tentar atualizar campeao`() {
        val uuid = UUID.randomUUID()
        every { mockedService.updateRequest(id = uuid.toString(), champion = champion) } returns response

        championController.updateChampion(id= uuid.toString(), championRequest = request).run {

            status shouldBe HttpStatus.OK
            body() shouldBe response
        }
        verify { mockedService.updateRequest(id = uuid.toString(), champion = champion) }
    }

    @Test
    private fun `deveria tentar deletar campeao`() {
        val uuid = UUID.randomUUID().toString()
        championController.deleteChampion(id= uuid).run {

            status shouldBe HttpStatus.NO_CONTENT
            body.isEmpty shouldBe true
        }
        verify { mockedService.deleteRequest(id = uuid) }
    }
}