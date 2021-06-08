package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("\${api.path}/champions")
class ChampionController {

    @Post
    fun createChampion(@Body championRequest: CreateChampionRequest): HttpResponse<Unit> {
        return HttpResponse.noContent()
    }
}