package br.com.iupp.buildingwarriors.core.service

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championToEntity
import br.com.iupp.buildingwarriors.core.model.Champion
import br.com.iupp.buildingwarriors.core.model.ChampionDifficulty.MODERATE
import br.com.iupp.buildingwarriors.core.model.ChampionRole.MAGE
import br.com.iupp.buildingwarriors.core.model.ChampionRole.TANK
import br.com.iupp.buildingwarriors.core.ports.ChampionRepositoryPort
import br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception.EntityNotFound
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

@MicronautTest
class ChampionServiceImplTest : AnnotationSpec() {

    private val mockedRepository = mockk<ChampionRepositoryPort>()

    private val service = ChampionServiceImpl(mockedRepository)

//    @Test
//    fun `deve encontrar campeao cadastrado`() {
//        val championId = UUID.randomUUID()
//        val champion = ChampionEntity(
//            name = "Ahri",
//            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
//            role = ChampionRole.MAGE,
//            difficulty = ChampionDifficulty.MODERATE
//        ).apply { id = championId }
//        every { mockedRepository.findById(championId) } returns Optional.of(champion)
//        val serviceResponse = service.getChampion(championId.toString())
//
//        with(serviceResponse) {
//            isPresent shouldBe true
//            with(get()) {
//                id shouldBe champion.id.toString()
//                name shouldBe champion.name
//                shortDescription shouldBe champion.shortDescription
//                role shouldBe champion.role
//                difficulty shouldBe champion.difficulty
//            }
//        }
//    }
//
//    @Test
//    fun `deve retornar optional vazio quando campeao nao estiver cadastrado`() {
//        every { mockedRepository.findById(any()) } returns Optional.empty()
//
//        val serviceResponse = service.getChampion(UUID.randomUUID().toString())
//
//        serviceResponse.isEmpty shouldBe true
//    }
//
//    @Test
//    fun `deve encontrar todos champions cadastrados`() {
//        val champions: List<ChampionEntity> = listOf(
//            ChampionEntity(
//                name = "Rammus",
//                shortDescription = "Idolatrado por muitos, dispensado por alguns e misterioso para todos, Rammus é um ser curioso e enigmático.",
//                role = TANK,
//                difficulty = ChampionDifficulty.MODERATE
//            ),
//            ChampionEntity(
//                name = "Morgana",
//                shortDescription = "Dividida entre sua natureza mortal e celestial, Morgana prendeu suas asas para preservar sua humanidade e inflige sua dor e amargura nos desonestos e corruptos.",
//                role = SUPPORT,
//                difficulty = ChampionDifficulty.LOW
//            ),
//            ChampionEntity(
//                name = "Ashe",
//                shortDescription = "A mãe de guerra Glacinata da tribo de Avarosa, Ashe comanda a horda mais populosa do norte.",
//                role = MARKSMAN,
//                difficulty = ChampionDifficulty.MODERATE
//            ),
//            ChampionEntity(
//                name = "Ahri",
//                shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
//                role = MAGE,
//                difficulty = ChampionDifficulty.MODERATE
//            ), ChampionEntity(
//                name = "Master Yi",
//                shortDescription = "Master Yi treinou seu corpo e afiou sua mente para que pensamento e ação se tornassem quase um só.",
//                role = ASSASSIN,
//                difficulty = ChampionDifficulty.MODERATE
//            ), ChampionEntity(
//                name = "Riven",
//                shortDescription = "Outrora mestra das espadas nos esquadrões de Noxus, agora Riven é uma expatriada em uma terra que um dia já tentou conquistar.",
//                role = FIGHTER,
//                difficulty = ChampionDifficulty.HIGH
//            )
//        )
//        every { mockedRepository.findAll() } returns champions
//
//        val functionReturn = service.getAllChampions()
//
//        val championsDetailsResponse = champions.map { ChampionResponse(it) }
//        functionReturn shouldContainExactlyInAnyOrder championsDetailsResponse
//    }

    @Test
    fun `deve cadastrar champion`() {
        val champion = Champion(
            name = "Ahri",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = MAGE,
            difficulty = MODERATE
        )
        every { mockedRepository.save(championToEntity(champion)) } returns champion

        service.saveChampion(champion)

        verify { mockedRepository.save(championToEntity(champion)) }
    }

    @Test
    fun `deve atualizar champion`() {
        val champion = Champion(
            id = UUID.randomUUID(),
            name = "Shen",
            shortDescription = "Com uma conexão inata com o poder latente de Runeterra, Ahri é uma vastaya capaz de transformar magia em orbes de pura energia.",
            role = TANK,
            difficulty = MODERATE
        )
        val updateChampionRequest = Champion(
            id = champion.id,
            name = "Ahri",
            role = MAGE
        )
        every { mockedRepository.findById(champion.id!!) } returns Optional.of(champion)
        every {
            mockedRepository.update(
                championToEntity(champion.apply {
                    name = updateChampionRequest.name
                    role = updateChampionRequest.role
                }
                ))
        } returns champion

        service.updateChampion(updateChampionRequest)

        verify { mockedRepository.update(championToEntity(champion)) }
    }

    @Test
    fun `deve lançar EntityNotFound quando não encontrar registro para atualizar `() {
        val updateChampionRequest = Champion(
            id = UUID.randomUUID(),
            name = "Ahri",
            role = MAGE
        )
        every { mockedRepository.findById(updateChampionRequest.id!!) } returns Optional.empty()

        val exception = shouldThrow<EntityNotFound> { service.updateChampion(updateChampionRequest) }

        verify { mockedRepository.findById(updateChampionRequest.id!!) }
        exception.entity shouldBe "champion"
    }

    @Test
    fun `deve deletar campeao`() {
        val champion = Champion(id = UUID.randomUUID())
        every { mockedRepository.deleteById(champion.id!!) } returns Unit

        service.deleteChampion(champion)

        verify { mockedRepository.deleteById(champion.id!!) }
    }
}