package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.controller.champion.request.CreateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.request.UpdateChampionRequest
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionCreatedResponse
import br.com.iupp.buildingwarriors.controller.champion.response.ChampionDetailsResponse
import br.com.iupp.buildingwarriors.exception.UniqueFieldAlreadyExistsException
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Positive

@Controller("\${api.path}/champions")
@Validated
class ChampionController(
    @Inject private val service: ChampionService,
    @Inject private val httpHostResolver: HttpHostResolver
) {

    @Post
    fun createChampion(
        httpRequest: HttpRequest<CreateChampionRequest>,
        @Body @Valid championRequest: CreateChampionRequest
    ): HttpResponse<ChampionCreatedResponse> {
        return try {
            val championCreated = service.saveChampion(championRequest.toModel())
            val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${championCreated.id}"
            HttpResponse.created(
                ChampionCreatedResponse(championCreated),
                HttpResponse.uri(location)
            )
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }

    @Get("/{id}")
    fun getChampion(@PathVariable @Positive(message = "Deve ser um numero positivo") id: Long): HttpResponse<ChampionDetailsResponse> {
        return try {
            val championOptional = service.getChampion(id)
            with(championOptional) {
                if (isPresent) return@with HttpResponse.ok(ChampionDetailsResponse(get()))
                HttpResponse.notFound()
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
    ): HttpResponse<ChampionDetailsResponse> {
        return try {
            val updatedChampion = service.updateChampion(id, updateChampionRequest)
            with(updatedChampion) {
                if (isPresent) {
                    val location = "${httpHostResolver.resolve(httpRequest)}/api/v1/champions/${get().id}"
                    return@with HttpResponse.accepted<ChampionDetailsResponse?>(
                        HttpResponse.uri(location)
                    ).body(ChampionDetailsResponse(get()))
                }
                return@with HttpResponse.notFound()
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
            return HttpResponse.noContent()
        } catch (e: Throwable) {
            HttpResponse.serverError()
        }
    }
}