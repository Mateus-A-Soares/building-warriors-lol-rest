package br.com.iupp.buildingwarriors.entrypoint.controller.response

import javax.validation.constraints.Size

data class ChampionResponse (
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 255)
    val shortDescription: String? = null,
    val role: String? = null,
    val difficulty: String? = null
)