package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@MicronautTest
class ChampionControllerDeleteTests {

    private val champion = Champion(
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE,
        difficulty = ChampionDifficulty.MODERATE
    ).apply { id = 1 }

    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    @Test
    fun `deve deletar champion existente`() {
        val controller = ChampionController(mockedService)

        val response = controller.deleteChampion(champion.id!!)

        assertEquals(HttpStatus.NO_CONTENT.code, response.status.code)
    }
}