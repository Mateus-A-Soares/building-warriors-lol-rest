package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToChampionEvent
import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToChampionResponse
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.core.ports.NatsServicePort
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class ChampionServiceImplTest : AnnotationSpec() {

    private val mockedNatsService = mockk<NatsServicePort>(relaxed = true)

    private val service = ChampionService(mockedNatsService)
    private val champion = Champion(
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE,
        difficulty = ChampionDifficulty.MODERATE
    )
    private val championEvent = championToChampionEvent(champion)

    @Test
    fun `deve retornar campeão após criar evento de persistencia`() {
        service.createRequest(champion) shouldBe championToChampionResponse(champion)
        verify { mockedNatsService.createChampionEvent(championEvent) }
    }

    @Test
    fun `deve retornar campeão após criar evento de atualização`() {
        val id = UUID.randomUUID().toString()
        service.updateRequest(id, champion) shouldBe championToChampionResponse(champion)
        verify { mockedNatsService.updateChampionEvent(championEvent.copy(id = id)) }
    }

    @Test
    fun `deve executar chamada de exclusão de campeão`() {
        val id = UUID.randomUUID().toString()
        service.deleteRequest(id)
        verify { mockedNatsService.deleteChampionEvent(id) }
    }
}