package br.com.iupp.buildingwarriors.entrypoint.listener.request

import br.com.iupp.buildingwarriors.entrypoint.listener.handler.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.entrypoint.listener.validator.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.entrypoint.listener.validator.ValidChampionRole
import br.com.iupp.buildingwarriors.entrypoint.listener.validator.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.Size

@Introspected
data class ChampionRequest(
    @field:ValidUUID
    val id: String? = null,
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 255)
    val shortDescription: String? = null,
    @field:ValidChampionRole
    val role: String? = null,
    @field:ValidChampionDifficulty
    val difficulty: String? = null
)
