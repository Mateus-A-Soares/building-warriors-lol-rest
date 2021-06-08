package br.com.iupp.buildingwarriors.repository

import br.com.iupp.buildingwarriors.model.Champion
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ChampionRepository : CrudRepository<Champion, Long>
