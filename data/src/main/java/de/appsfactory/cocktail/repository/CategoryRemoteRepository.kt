package de.appsfactory.cocktail.repository

import androidx.annotation.WorkerThread
import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.connectivity.Connectivity
import de.appsfactory.cocktail.entity.Category
import de.appsfactory.cocktail.entity.Icon
import de.appsfactory.cocktail.rest.TheCocktailDBService
import de.appsfactory.cocktail.rest.model.ApiCategory

class CategoryRemoteRepository(
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : CategoryRepository {

    @WorkerThread
    override fun getCategories(): Result<List<Category>> {
        return when (val result =
            callApi(connectivity, restService.getCategories())) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    private fun map(apiList: List<ApiCategory>?): Success<List<Category>> {
        return if (apiList != null) {
            Success(
                apiList.asSequence()
                    .map { it.strCategory }
                    .filterNotNull()
                    .map { Category(it, iconFromName(it)) }
                    .toList()
            )
        } else {
            Success(emptyList())
        }
    }

    private fun iconFromName(name: String): Icon {
        return when (name) {
            "Cocktail" -> Icon.COCKTAIL
            "Shot" -> Icon.SHOT
            "Coffee / Tea" -> Icon.HOT_BEVERAGE
            "Beer" -> Icon.BEER
            "Homemade Liqueur" -> Icon.HOMEMADE
            "Punch / Party Drink" -> Icon.PARTY
            else -> Icon.OTHER
        }
    }

}