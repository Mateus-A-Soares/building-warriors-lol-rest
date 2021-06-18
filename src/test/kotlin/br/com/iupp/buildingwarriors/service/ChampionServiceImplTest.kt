package br.com.iupp.buildingwarriors.service

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionServiceImplTest : AnnotationSpec() {

    private val mockedRepository = mockk<ChampionRepository>()

    private val service = ChampionServiceImpl(mockedRepository)

    @Test
    fun `deve cadastrar champion`() {
        val championId = 1L
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE)
        val request = mockk<ChampionRequest>()
        every { request.toModel(mockedRepository) } returns champion.apply { id = championId }
        every { mockedRepository.save(champion) } returns champion

        val serviceResponse = service.saveChampion(request)

        with(serviceResponse) {
            championId shouldBe id
            champion.name shouldBe name
            champion.shortDescription shouldBe shortDescription
            champion.role shouldBe role
            champion.difficulty shouldBe difficulty
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
        every { mockedRepository.findById(champion.id!!) } returns Optional.of(champion)
        val serviceResponse = service.getChampion(champion.id!!)

        with(serviceResponse) {
            isPresent shouldBe true
            with(get()) {
                id shouldBe champion.id
                champion.name shouldBe name
                champion.shortDescription shouldBe shortDescription
                champion.role shouldBe role
                champion.difficulty shouldBe difficulty
            }
        }
    }

    @Test
    fun `deve retornar optional vazio quando campeao nao estiver cadastrado`() {
        every { mockedRepository.findById(any()) } returns Optional.empty()

        val serviceResponse = service.getChampion(1)

        serviceResponse.isEmpty shouldBe true
    }

    @Test
    fun `deve atualizar champion`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = 1 }
        val updateRequest = ChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        every { mockedRepository.findById(champion.id!!) } returns Optional.of(champion)
        every { mockedRepository.existsByName(updateRequest.name!!) } returns false

        val serviceResponse = service.updateChampion(1, updateRequest)

        serviceResponse.isPresent shouldBe true
        with(serviceResponse.get()) {
            champion.id shouldBe id
            (if (updateRequest.name.isNullOrBlank()) updateRequest.name else champion.name) shouldBe name
            (if (updateRequest.shortDescription.isNullOrBlank()) updateRequest.shortDescription else champion.shortDescription) shouldBe shortDescription
            (if (updateRequest.role.isNullOrBlank()) updateRequest.role else champion.role) shouldBe role
            (if (updateRequest.difficulty.isNullOrBlank()) updateRequest.difficulty else champion.difficulty) shouldBe difficulty
        }
    }

    @Test
    fun `deve retornar optional vazio quando nao encontrar champion para atualizar`() {
        val updateRequest = ChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        every { mockedRepository.findById(any()) } returns Optional.empty()

        val serviceResponse = service.updateChampion(1, updateRequest)

        serviceResponse.isEmpty shouldBe true
    }

    @Test
    fun `deve retornar lancar UniqueFieldAlreadyExistsException quando encontrar champion diferente com o nome a ser atualizado`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = 1 }
        val updateRequest = ChampionRequest(name = "Riven")
        every { mockedRepository.findById(1) } returns Optional.of(champion)
        every { mockedRepository.existsByName(updateRequest.name!!) } returns true
        val exception = shouldThrow<UniqueFieldAlreadyExistsException> {
            service.updateChampion(1, updateRequest)
        }

        "champion" shouldBe exception.entity
        "name" shouldBe exception.field
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
        every { mockedRepository.findAll() } returns champions

        val functionReturn = service.getAllChampions()

        val championsDetailsResponse = champions.map { ChampionResponse(it) }
        functionReturn shouldContainExactlyInAnyOrder championsDetailsResponse
    }

    @Test
    fun `deve deletar campeao`() {
        val championId = 1L
        every { mockedRepository.deleteById(championId) } returns Unit
        service.deleteChampion(championId)
    }
}