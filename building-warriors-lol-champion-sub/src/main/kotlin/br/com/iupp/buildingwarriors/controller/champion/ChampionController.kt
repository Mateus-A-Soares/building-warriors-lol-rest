package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.service.ChampionService
import br.com.iupp.buildingwarriors.util.validator.ValidUUID
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@Controller("\${api.path}/champions")
@Validated
class ChampionController(
    private val service: ChampionService
) {

    @Post
    fun createChampion(
        httpRequest: HttpRequest<ChampionRequest>,
        @Body @Valid championRequest: ChampionRequest
    ): HttpResponse<ChampionResponse> {
        val body = service.saveChampion(championRequest)
        val location = "${httpRequest.path}/${body.id}"
        return HttpResponse.created(body, HttpResponse.uri(location))
    }

    @Get
    fun getAllChampions(): HttpResponse<List<ChampionResponse>> = HttpResponse.ok(service.getAllChampions())

    @Get("/{id}")
    fun getChampion(@PathVariable @NotBlank id: String): HttpResponse<ChampionResponse> =
        with(service.getChampion(id)) {
            if (isPresent) HttpResponse.ok(get())
            else HttpResponse.notFound()
        }


    @Put("/{id}")
    fun updateChampion(
        httpRequest: HttpRequest<ChampionRequest>,
        @Valid @Body championRequest: ChampionRequest,
        @PathVariable @NotBlank @ValidUUID id: String
    ): HttpResponse<ChampionResponse> = with(service.updateChampion(id, championRequest)) {
        if (isEmpty) HttpResponse.notFound()
        else {
            val location = "${httpRequest.path}/${get().id}"
            HttpResponse.accepted<ChampionResponse?>(HttpResponse.uri(location)).body(get())
        }
    }

    @Delete("/{id}")
    fun deleteChampion(@PathVariable @NotBlank @ValidUUID id: String): HttpResponse<Unit> {
        service.deleteChampion(id)
        return HttpResponse.noContent()
    }
}