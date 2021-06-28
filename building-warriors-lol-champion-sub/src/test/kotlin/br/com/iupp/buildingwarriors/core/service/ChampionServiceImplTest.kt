package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.entrypoint.listener.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionEntity
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionDifficulty
import br.com.iupp.buildingwarriors.infrastructure.repository.entity.ChampionRole
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import io.kotest.core.spec.style.AnnotationSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import java.util.*

@MicronautTest
class ChampionServiceImplTest : AnnotationSpec() {

    private val mockedRepository = mockk<ChampionRepositoryPort>()

    private val service = ChampionServiceImpl(mockedRepository)

    @Test
    fun `deve cadastrar champion`() {
        val championId = UUID.randomUUID()
        val champion = ChampionEntity(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        )
        val request = mockk<ChampionRequest>()
        every { request.toModel() } returns champion.apply { id = championId }
        every { mockedRepository.save(champion) } returns champion

        val serviceResponse = service.saveChampion(request)

        with(serviceResponse) {
            id shouldBe championId.toString()
            name shouldBe champion.name
            shortDescription shouldBe champion.shortDescription
            role shouldBe champion.role
            difficulty shouldBe champion.difficulty
        }
    }

    @Test
    fun `deve encontrar campeao cadastrado`() {
        val championId = UUID.randomUUID()
        val champion = ChampionEntity(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = championId }
        every { mockedRepository.findById(championId) } returns Optional.of(champion)
        val serviceResponse = service.getChampion(championId.toString())

        with(serviceResponse) {
            isPresent shouldBe true
            with(get()) {
                id shouldBe champion.id.toString()
                name shouldBe champion.name
                shortDescription shouldBe champion.shortDescription
                role shouldBe champion.role
                difficulty shouldBe champion.difficulty
            }
        }
    }

    @Test
    fun `deve retornar optional vazio quando campeao nao estiver cadastrado`() {
        every { mockedRepository.findById(any()) } returns Optional.empty()

        val serviceResponse = service.getChampion(UUID.randomUUID().toString())

        serviceResponse.isEmpty shouldBe true
    }

    @Test
    fun `deve atualizar champion`() {
        val championId = UUID.randomUUID()
        val champion = ChampionEntity(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = ChampionRole.MAGE,
            difficulty = ChampionDifficulty.MODERATE
        ).apply { id = championId }
        val updateRequest = ChampionRequest(
            name = "Riven",
            shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
            role = "FIGHTER",
            difficulty = "HIGH"
        )
        every { mockedRepository.findById(championId) } returns Optional.of(champion)
        every { mockedRepository.update(champion) } returns champion

        val serviceResponse = service.updateChampion(championId.toString(), updateRequest)

        serviceResponse.isPresent shouldBe true
        with(serviceResponse.get()) {
            id shouldBe champion.id.toString()
            name shouldBe (if (updateRequest.name.isNullOrBlank()) updateRequest.name else champion.name)
            shortDescription shouldBe (if (updateRequest.shortDescription.isNullOrBlank()) updateRequest.shortDescription else champion.shortDescription)
            role shouldBe (if (updateRequest.role.isNullOrBlank()) updateRequest.role else champion.role)
            difficulty shouldBe (if (updateRequest.difficulty.isNullOrBlank()) updateRequest.difficulty else champion.difficulty)
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

        val serviceResponse = service.updateChampion(UUID.randomUUID().toString(), updateRequest)

        serviceResponse.isEmpty shouldBe true
    }

    @Test
    fun `deve encontrar todos champions cadastrados`() {
        val champions: List<ChampionEntity> = listOf(
            ChampionEntity(
                name = "Rammus",
                shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
                role = ChampionRole.TANK,
                difficulty = ChampionDifficulty.MODERATE
            ),
            ChampionEntity(
                name = "Morgana",
                shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
                role = ChampionRole.SUPPORT,
                difficulty = ChampionDifficulty.LOW
            ),
            ChampionEntity(
                name = "Ashe",
                shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
                role = ChampionRole.MARKSMAN,
                difficulty = ChampionDifficulty.MODERATE
            ),
            ChampionEntity(
                name = "Ahri",
                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
                role = ChampionRole.MAGE,
                difficulty = ChampionDifficulty.MODERATE
            ), ChampionEntity(
                name = "Master Yi",
                shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
                role = ChampionRole.ASSASSIN,
                difficulty = ChampionDifficulty.MODERATE
            ), ChampionEntity(
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
        val championId = UUID.randomUUID()
        every { mockedRepository.deleteById(championId) } returns Unit
        service.deleteChampion(championId.toString())
    }
}