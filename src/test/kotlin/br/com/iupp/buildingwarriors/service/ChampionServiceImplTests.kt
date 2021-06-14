package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class ChampionServiceImplTests {

    private val mockedRepository: ChampionRepository = Mockito.mock(ChampionRepository::class.java)

    private val service = ChampionServiceImpl(mockedRepository)

    @Test
    fun `deve cadastrar champion`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        )
        `when`(mockedRepository.save(champion)).thenReturn(champion.apply { id = 1 })

        val serviceResponse = service.saveChampion(champion)

        with(serviceResponse) {
            assertEquals(1, id)
            assertEquals(champion.name, name)
            assertEquals(champion.shortDescription, shortDescription)
            assertEquals(champion.role, role)
            assertEquals(champion.difficulty, difficulty)
        }
    }

    @Test
    fun `deve encontrar campeao cadastrado`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = 1 }
        `when`(mockedRepository.findById(champion.id!!)).thenReturn(Optional.of(champion))

        val serviceResponse = service.getChampion(champion.id!!)

        with(serviceResponse) {
            assertTrue(isPresent)
            with(get()) {
                assertEquals(1, id)
                assertEquals(champion.name, name)
                assertEquals(champion.shortDescription, shortDescription)
                assertEquals(champion.role, role)
                assertEquals(champion.difficulty, difficulty)
            }
        }
    }

    @Test
    fun `deve retornar optional vazio quando campeao nao estiver cadastrado`() {
        `when`(mockedRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty())

        val serviceResponse = service.getChampion(1)

        assertTrue(serviceResponse.isEmpty)
    }

    @Test
    fun `deve atualizar champion`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = 1 }
        val updateRequest = UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        `when`(mockedRepository.findById(champion.id!!)).thenReturn(Optional.of(champion))
        `when`(mockedRepository.existsByName(updateRequest.name!!)).thenReturn(false)

        val serviceResponse = service.updateChampion(1, updateRequest)

        assertTrue(serviceResponse.isPresent)
        with(serviceResponse.get()) {
            assertEquals(champion.id, id)
            assertEquals(
                if (updateRequest.name.isNullOrBlank()) updateRequest.name else champion.name,
                name
            )
            assertEquals(
                if (updateRequest.shortDescription.isNullOrBlank()) updateRequest.shortDescription else champion.shortDescription,
                shortDescription
            )
            assertEquals(
                if (updateRequest.role.isNullOrBlank()) updateRequest.role else champion.role,
                role
            )
            assertEquals(
                if (updateRequest.difficulty.isNullOrBlank()) updateRequest.difficulty else champion.difficulty,
                difficulty
            )
        }
    }

    @Test
    fun `deve retornar optional vazio quando nao encontrar champion para atualizar`() {
        val updateRequest = UpdateChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        `when`(mockedRepository.findById(1)).thenReturn(Optional.empty())

        val serviceResponse = service.updateChampion(1, updateRequest)

        assertTrue(serviceResponse.isEmpty)
    }

    @Test
    fun `deve retornar lancar UniqueFieldAlreadyExistsException quando encontrar champion diferente com o nome a ser atualizado`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = 1 }
        val updateRequest = UpdateChampionRequest(name = "Riven")
        `when`(mockedRepository.findById(1)).thenReturn(Optional.of(champion))
        `when`(mockedRepository.existsByName(updateRequest.name!!)).thenReturn(true)
        val exception = assertThrows<UniqueFieldAlreadyExistsException> {
            service.updateChampion(1, updateRequest)
        }

        assertEquals("champion", exception.entity)
        assertEquals("name", exception.field)
    }

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        val champions: List<Champion> = listOf(
            Champion(
                name = "Rammus",
                shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
                role = ChampionRole.TANK,
                difficulty = ChampionDifficulty.MODERATE
            ),
            Champion(
                name = "Morgana",
                shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
                role = ChampionRole.SUPPORT,
                difficulty = ChampionDifficulty.LOW
            ),
            Champion(
                name = "Ashe",
                shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
                role = ChampionRole.MARKSMAN,
                difficulty = ChampionDifficulty.MODERATE
            ),
            Champion(
                name = "Ahri",
                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
                role = ChampionRole.MAGE,
                difficulty = ChampionDifficulty.MODERATE
            ), Champion(
                name = "Master Yi",
                shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
                role = ChampionRole.ASSASSIN,
                difficulty = ChampionDifficulty.MODERATE
            ), Champion(
                name = "Riven",
                shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
                role = ChampionRole.FIGHTER,
                difficulty = ChampionDifficulty.HIGH
            )
        )
        `when`(mockedRepository.findAll())
            .thenReturn(champions)

        val functionReturn = service.getAllChampions()

        val championsDetailsResponse = champions.map { ChampionResponse(it) }
        MatcherAssert.assertThat(functionReturn, Matchers.containsInAnyOrder(*championsDetailsResponse.toTypedArray()))
    }

    @Test
    fun `deve deletar campeao`() = service.deleteChampion(1)
}