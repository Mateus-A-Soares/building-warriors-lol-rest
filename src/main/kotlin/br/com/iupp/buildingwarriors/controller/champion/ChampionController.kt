package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.ChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionResponse
import br.com.iupp.buildingwarriors.controller.handler.response.DefaultErrorResponse
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.validation.Validated
import javax.validation.ConstraintViolationException
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
        val body = service.saveChampion(championRequest.toModel())
        val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${body.id}"
        return HttpResponse.created(body, HttpResponse.uri(location))
    }

    @Get
    fun getAllChampions(): HttpResponse<List<ChampionResponse>> = HttpResponse.ok(service.getAllChampions())

    @Get("/{id}")
    fun getChampion(@PathVariable @Positive(message = "Deve ser um numero positivo") id: Long): HttpResponse<ChampionResponse> =
        with(service.getChampion(id)) {
            if (isPresent) HttpResponse.ok(get())
            else HttpResponse.notFound()
        }


    @Put("/{id}")
    fun updateChampion(
        httpRequest: HttpRequest<UpdateChampionRequest>,
        @PathVariable @Positive(message = "Deve ser um numero positivo") id: Long,
        @Body @Valid updateChampionRequest: UpdateChampionRequest
    ): HttpResponse<ChampionResponse> = with(service.updateChampion(id, updateChampionRequest)) {
        if (isEmpty) HttpResponse.notFound()
        else {
            val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${get().id}"
            HttpResponse.accepted<ChampionResponse?>(HttpResponse.uri(location)).body(get())
        }
    }

    @Delete("/{id}")
    fun deleteChampion(@PathVariable @Positive(message = "Deve ser um numero positivo") id: Long): HttpResponse<Unit> {
        service.deleteChampion(id)
        return HttpResponse.noContent()
    }

    @Error(exception = ConstraintViolationException::class)
    fun constraintException(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<DefaultErrorResponse> {
        val messages = exception.constraintViolations.map {
            "${it.propertyPath}: ${it.message}"
        }
        return HttpResponse.unprocessableEntity<DefaultErrorResponse>().body(DefaultErrorResponse(
            statusCode = HttpStatus.UNPROCESSABLE_ENTITY.code,
            messages = messages,
            path = request.path,
            error = HttpStatus.UNPROCESSABLE_ENTITY.reason
        ))
    }
}