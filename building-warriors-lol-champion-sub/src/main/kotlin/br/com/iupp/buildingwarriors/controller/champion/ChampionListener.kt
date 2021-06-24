package br.com.iupp.buildingwarriors.controller.champion

import br.com.iupp.buildingwarriors.model.ChampionEvent
import br.com.iupp.buildingwarriors.model.Operation
import br.com.iupp.buildingwarriors.service.ChampionService
import io.micronaut.messaging.annotation.MessageBody
import io.micronaut.nats.annotation.NatsListener
import io.micronaut.nats.annotation.Subject
import io.micronaut.validation.Validated

@Validated
@NatsListener
class ChampionListener(private val service: ChampionService) {

    @Subject("champion")
    fun getMessage(@MessageBody championEvent: ChampionEvent) {
        with(championEvent) {
            when (operation) {
                Operation.CREATE -> service.saveChampion(championRequest)
                Operation.UPDATE -> service.updateChampion(championRequest)
                Operation.DELETE -> service.deleteChampion(championRequest)
            }
        }
    }
}