package br.com.iupp.buildingwarriors.listener.champion.request

import br.com.iupp.buildingwarriors.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.util.validator.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.util.validator.ValidChampionRole
import br.com.iupp.buildingwarriors.util.validator.ValidUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.Size

@Introspected
data class ChampionRequest(
    @field:ValidUUID
    val id: String? = null,
    @field:Size(max = 50)
    val name: String? = null,
    @field:Size(max = 255)
    val shortDescription: String? = null,
    @field:ValidChampionRole
    val role: String? = null,
    @field:ValidChampionDifficulty
    val difficulty: String? = null
) {

    fun toModel(): Champion {
        validateFields()
        return Champion(
            id = id?.let(UUID::fromString),
            name = name!!,
            shortDescription = shortDescription!!,
            role = ChampionRole.valueOf(role!!.toUpperCase()),
            difficulty = ChampionDifficulty.valueOf(difficulty!!.toUpperCase())
        )
    }

    /**
     * @throws FieldConstraintException
     */
    private fun validateFields() {
        val fieldErrors: MutableList<Pair<String, String>> = mutableListOf()
        if (name.isNullOrBlank() || shortDescription.isNullOrBlank() || role.isNullOrBlank() || difficulty.isNullOrBlank()) {
            if (name.isNullOrBlank()) fieldErrors.add("name" to "Campo não pode estar vazio")
            if (shortDescription.isNullOrBlank()) fieldErrors.add("shortDescription" to "Campo não pode estar vazio")
            if (role.isNullOrBlank()) fieldErrors.add("role" to "Campo não pode estar vazio")
            if (difficulty.isNullOrBlank()) fieldErrors.add("difficulty" to "Campo não pode estar vazio")
        }
        if (fieldErrors.isNotEmpty()) throw FieldConstraintException(
            entity = "champion",
            fieldErrors = fieldErrors
        )
    }
}
