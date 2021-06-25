package br.com.iupp.buildingwarriors.entrypoint.controller.request

import br.com.iupp.buildingwarriors.entrypoint.utils.validator.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

@Introspected
data class ChampionRequest(
    @field:ValidUUID
    val id: String? = null,
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 255)
    val shortDescription: String? = null,
    val role: String? = null,
    val difficulty: String? = null
)
