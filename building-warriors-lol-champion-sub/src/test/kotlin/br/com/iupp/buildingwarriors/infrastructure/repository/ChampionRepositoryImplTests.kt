package br.com.iupp.buildingwarriors.infrastructure.repository

import com.datastax.oss.driver.api.core.CqlSession
import io.kotest.core.spec.style.AnnotationSpec
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.mockk

@MicronautTest
class ChampionRepositoryImplTests : AnnotationSpec() {

    private val cqlSession: CqlSession = mockk<CqlSession>()

    private val championRepositoryImpl = ChampionRepositoryImpl(cqlSession)
}