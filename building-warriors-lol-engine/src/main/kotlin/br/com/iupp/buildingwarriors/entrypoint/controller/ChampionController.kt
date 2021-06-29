package br.com.iupp.buildingwarriors.entrypoint.controller

import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.controller.response.ChampionResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("\${api.path}/champions")
class ChampionController(private val championService: ChampionServicePort) {

    @Get
    fun getAll(): HttpResponse<List<ChampionResponse>> = HttpResponse.ok(championService.findAll())
}