package br.com.iupp.buildingwarriors.entrypoint.listener

import br.com.iupp.buildingwarriors.core.mapper.ChampionMapper.championEventToModel
import br.com.iupp.buildingwarriors.core.ports.ChampionServicePort
import br.com.iupp.buildingwarriors.entrypoint.listener.request.ChampionEvent
import br.com.iupp.buildingwarriors.entrypoint.listener.request.Operation
import io.micronaut.messaging.annotation.MessageBody
import io.micronaut.nats.annotation.NatsListener
import io.micronaut.nats.annotation.Subject
import io.micronaut.validation.Validated

@Validated
@NatsListener
class ChampionListener(private val service: ChampionServicePort) {

    @Subject("champion")
    fun getMessage(@MessageBody championEvent: ChampionEvent) {
        with(championEvent) {
            when (operation) {
                Operation.CREATE -> service.saveChampion(championEventToModel(championRequest))
                Operation.UPDATE -> service.updateChampion(championEventToModel(championRequest))
                Operation.DELETE -> service.deleteChampion(championEventToModel(championRequest))
            }
        }
    }
}