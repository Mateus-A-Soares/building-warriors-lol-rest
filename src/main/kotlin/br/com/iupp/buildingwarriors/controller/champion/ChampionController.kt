package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.Validator

@Controller("\${api.path}/champions")
@Validated
class ChampionController(@Inject val validator: Validator) {

    @Post
    fun createChampion(@Body @Valid championRequest: CreateChampionRequest): HttpResponse<Unit> {
        return HttpResponse.noContent()
    }
}