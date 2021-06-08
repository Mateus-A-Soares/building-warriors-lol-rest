package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.lang.Exception
import javax.inject.Inject
import javax.validation.Valid

@Controller("\${api.path}/champions")
@Validated
class ChampionController(@Inject val service: ChampionService) {

    @Post
    fun createChampion(@Body @Valid championRequest: CreateChampionRequest): HttpResponse<ChampionCreatedResponse> {
        return try {
            val championCreated = service.saveChampion(championRequest.toModel())
            HttpResponse.ok(ChampionCreatedResponse(championCreated))
        } catch (e: Throwable){
            HttpResponse.serverError()
        }
    }
}