package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.connectivity.Connectivity
import de.appsfactory.cocktail.entity.Ingredient
import de.appsfactory.cocktail.rest.TheCocktailDBService
import de.appsfactory.cocktail.rest.model.ApiIngredient

class IngredientRemoteRepository (
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : IngredientRepository {

    override fun getIngredients(): Result<List<Ingredient>> {
        return when (val result =
            callApi(connectivity, restService.getIngredients())) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    private fun map(apiList: List<ApiIngredient>?): Success<List<Ingredient>> {
        return if (apiList != null) {
            Success(
                apiList.asSequence()
                    .map { it.strIngredient1 }
                    .filterNotNull()
                    .map { Ingredient(it) }
                    .toList()
            )
        } else {
            Success(emptyList())
        }
    }
}