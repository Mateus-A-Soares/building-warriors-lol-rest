package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.validation.Validated
import javax.validation.Valid
import javax.validation.constraints.Positive

@Controller("\${api.path}/champions")
@Validated
class ChampionController(
    private val service: ChampionService,
    private val httpHostResolver: HttpHostResolver
) {

    @Post
    fun createChampion(
        httpRequest: HttpRequest<ChampionRequest>,
        @Body @Valid championRequest: ChampionRequest
    ): HttpResponse<ChampionResponse> {
        return try {
            val body = service.saveChampion(championRequest.toModel())
            val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${body.id}"
            HttpResponse.created(body, HttpResponse.uri(location))
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }

    @Get
    fun getAllChampions(): HttpResponse<List<ChampionResponse>> {
        return try {
            return HttpResponse.ok(service.getAllChampions())
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }

    @Get("/{id}")
    fun getChampion(@PathVariable @Positive(message = "Deve ser um numero positivo") id: Long): HttpResponse<ChampionResponse> {
        return try {
            with(service.getChampion(id)) {
                if (isPresent) HttpResponse.ok(get())
                else HttpResponse.notFound()
            }
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }

    @Put("/{id}")
    fun updateChampion(
        httpRequest: HttpRequest<UpdateChampionRequest>,
        @PathVariable @Positive(message = "Deve ser um numero positivo") id: Long,
        @Body @Valid updateChampionRequest: UpdateChampionRequest
    ): HttpResponse<ChampionResponse> {
        return try {
            with(service.updateChampion(id, updateChampionRequest)) {
                if (isEmpty) HttpResponse.notFound()
                else {
                    val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${get().id}"
                    HttpResponse.accepted<ChampionResponse?>(HttpResponse.uri(location)).body(get())
                }
            }
        } catch (e: UniqueFieldAlreadyExistsException) {
            HttpResponse.unprocessableEntity()
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }

    @Delete("/{id}")
    fun deleteChampion(@PathVariable @Positive(message = "Deve ser um numero positivo") id: Long): HttpResponse<Unit> {
        return try {
            service.deleteChampion(id)
            HttpResponse.noContent()
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }
}