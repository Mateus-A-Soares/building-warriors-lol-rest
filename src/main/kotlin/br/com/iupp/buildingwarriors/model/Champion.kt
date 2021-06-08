package br.com.iupp.buildingwarriors.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
class Champion(
    @field: NotBlank @field:Size(max = 50)
    @Column(unique = true, nullable = false, length = 50)
    val name: String,

    @field:NotBlank @field:Size(max = 255)
    @Column(nullable = false, length = 255)
    val shortDescription: String,

    @field:NotBlank
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    val role: ChampionRole,

    @field:NotBlank
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    val difficulty: ChampionDifficulty
) {
    @Id
    @GeneratedValue
    var id : Long? = null
}

enum class ChampionDifficulty {
    LOW, MODERATE, HIGH
}

enum class ChampionRole {
    ASSASSIN,
    FIGHTER,
    MAGE,
    MARKSMAN,
    SUPPORT,
    TANK
}
