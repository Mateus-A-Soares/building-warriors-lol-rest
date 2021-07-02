package br.com.iupp.buildingwarriors.infrastructure.service

import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.core.model.ChampionRole
import br.com.iupp.buildingwarriors.infrastructure.client.ChampionClient
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEvent
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionEventInformation
import br.com.iupp.buildingwarriors.infrastructure.model.ChampionOperations.*
import io.kotest.core.spec.style.AnnotationSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class NatsServiceTests : AnnotationSpec() {

    private val mockedChampionClient = mockk<ChampionClient>(relaxed = true)
    private val natsService = NatsService(mockedChampionClient)
    private val championEvent = ChampionEvent(
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE.toString(),
        difficulty = ChampionDifficulty.MODERATE.toString()
    )

    @Test
    fun `deve tentar criar um evento de persistencia`() {
        natsService.createChampionEvent(championEvent)
        verify { mockedChampionClient.publishEvent(ChampionEventInformation(CREATE, championEvent)) }
    }

    @Test
    fun `deve tentar criar um evento de atualizacao`() {
        val id = UUID.randomUUID().toString()
        natsService.updateChampionEvent(championEvent.copy(id = id))
        verify { mockedChampionClient.publishEvent(ChampionEventInformation(UPDATE, championEvent.copy(id = id))) }
    }

    @Test
    fun `deve tentar criar um evento de exclusao`() {
        val id = UUID.randomUUID().toString()
        natsService.deleteChampionEvent(id)
        verify { mockedChampionClient.publishEvent(ChampionEventInformation(DELETE, ChampionEvent(id = id))) }
    }
}