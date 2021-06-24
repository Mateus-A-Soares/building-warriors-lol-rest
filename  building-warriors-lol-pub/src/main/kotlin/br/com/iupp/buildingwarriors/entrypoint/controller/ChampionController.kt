package br.com.iupp.buildingwarriors.entrypoint.controller

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper
import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championRequestToChampion
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.request.ChampionRequest
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller("\${api.path}/champions")
@Validated
class ChampionController(
    private val service: ChampionServicePort
) {

    @Post
    fun createChampion(
        @Body @Valid championRequest: ChampionRequest
    ): HttpResponse<ChampionResponse> =
        HttpResponse.ok(service.createRequest(championRequestToChampion(championRequest)))

    @Put("/{id}")
    fun updateChampion(
        @Valid @Body championRequest: ChampionRequest
    ): HttpResponse<ChampionResponse> =
        HttpResponse.ok(service.updateRequest(championRequestToChampion(championRequest)))

    @Delete("/{id}")
    fun deleteChampion(@PathVariable @NotBlank id: String): HttpResponse<Unit> {
        service.deleteRequest(id)
        return HttpResponse.noContent()
    }
}