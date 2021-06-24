package br.com.iupp.buildingwarriors.entrypoint.controller

import br.com.iupp.buildingwarriors.entrypoint.controller.request.ChampionRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller("\${api.path}/champions")
@Validated
class ChampionController {

    @Post
    fun createChampion(
        httpRequest: HttpRequest<ChampionRequest>,
        @Body @Valid championRequest: ChampionRequest
    ): HttpResponse<Unit> {
        championRequest.toString().let(::println)
        return HttpResponse.noContent()
    }

    @Put("/{id}")
    fun updateChampion(
        httpRequest: HttpRequest<ChampionRequest>,
        @Valid @Body championRequest: ChampionRequest,
        @PathVariable @NotBlank id: String
    ): HttpResponse<Unit> {
        championRequest.toString().let(::println)
        return HttpResponse.noContent()
    }

    @Delete("/{id}")
    fun deleteChampion(@PathVariable @NotBlank id: String): HttpResponse<Unit> {
        id.let(::println)
        return HttpResponse.noContent()
    }
}