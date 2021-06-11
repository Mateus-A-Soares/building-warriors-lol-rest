package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.util.HttpHostResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class DeleteChampionTests {

    private val champion = Champion(
        name = "Ahri",
        shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
        role = ChampionRole.MAGE,
        difficulty = ChampionDifficulty.MODERATE
    )

    @Mock
    private val mockedService: ChampionService = Mockito.mock(ChampionService::class.java)

    @Mock
    private val mockedHttpHostResolver: HttpHostResolver = Mockito.mock(HttpHostResolver::class.java)

    @Test
    fun `deve deletar champion existente`() {
        val controller = ChampionController(mockedService, mockedHttpHostResolver)

        val response = controller.deleteChampion(champion.id!!)

        assertEquals(HttpStatus.NO_CONTENT.code, response.status.code)
    }

    @Test
    fun `deve retornar status 500 quando Exception inesperada for lancada`() {
        `when`(mockedService.deleteChampion(champion.id!!))
            .thenThrow(RuntimeException())
        val controller = ChampionController(mockedService, mockedHttpHostResolver)

        val response = controller.deleteChampion(champion.id!!)

        assertEquals(HttpStatus.NO_CONTENT.code, response.status.code)
    }
}