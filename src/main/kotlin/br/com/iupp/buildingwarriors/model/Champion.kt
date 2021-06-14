package br.com.iupp.buildingwarriors.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class Champion(
    @field: NotBlank @field:Size(max = 50)
    @Column(unique = true, nullable = false, length = 50)
    var name: String,

    @field:NotBlank @field:Size(max = 255)
    @Column(nullable = false, length = 255)
    var shortDescription: String,

    @field:NotBlank
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    var role: ChampionRole,

    @field:NotBlank
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    var difficulty: ChampionDifficulty,

    @Id
    @GeneratedValue
    var id : Long? = null
)
