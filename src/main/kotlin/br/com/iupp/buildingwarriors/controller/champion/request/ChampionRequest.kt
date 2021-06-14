package br.com.iupp.buildingwarriors.controller.champion.request

import br.com.iupp.buildingwarriors.exception.FieldConstraintException
import br.com.iupp.buildingwarriors.model.Champion
import br.com.iupp.buildingwarriors.model.ChampionDifficulty
import br.com.iupp.buildingwarriors.model.ChampionRole
import br.com.iupp.buildingwarriors.repository.ChampionRepository
import br.com.iupp.buildingwarriors.util.validator.ValidChampionDifficulty
import br.com.iupp.buildingwarriors.util.validator.ValidChampionRole
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

@Introspected
data class ChampionRequest(
    @field:Size(max = 50)
    val name: String?,
    @field:Size(max = 255)
    val shortDescription: String?,
    @field:ValidChampionRole
    val role: String?,
    @field:ValidChampionDifficulty
    val difficulty: String?
) {

    fun toModel(repository: ChampionRepository): Champion {
        validateFields(repository)
        return Champion(
            name = name!!,
            shortDescription = shortDescription!!,
            role = ChampionRole.valueOf(role!!.toUpperCase()),
            difficulty = ChampionDifficulty.valueOf(difficulty!!.toUpperCase())
        )
    }

    /**
     * @throws FieldConstraintException
     */
    private fun validateFields(repository: ChampionRepository) {
        val fieldErrors: MutableList<Pair<String, String>> = mutableListOf()
        name?.let {
            if (repository.existsByName(name))
                fieldErrors.add("name" to "já cadastrado")
        }
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
