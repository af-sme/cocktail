package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.connectivity.Connectivity
import de.appsfactory.cocktail.db.DrinkDao
import de.appsfactory.cocktail.db.model.DrinkEntity
import de.appsfactory.cocktail.entity.DetailedDrink
import de.appsfactory.cocktail.entity.DetailedDrink.Companion.NO_DETAILS
import de.appsfactory.cocktail.rest.TheCocktailDBService
import de.appsfactory.cocktail.rest.model.ApiDetailedDrink

class DetailedDrinkComposeRepository(
    private val drinkDao: DrinkDao,
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : DetailedDrinkRepository {


    override fun getDetailedDrink(id: String): Result<DetailedDrink> {
        return when (val result =
            callApi(connectivity, restService.getDrinkById(id))) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    override fun addFavoriteDrink(drink: DetailedDrink): Result<DetailedDrink> {
        drinkDao.addFavorite(
            drinkToDb(drink)
        )
        return Success(drink.copy(isFavorite = true))
    }

    override fun removeFavoriteDrink(drink: DetailedDrink): Result<DetailedDrink> {
        drinkDao.removeFavorite(
            drinkToDb(drink)
        )
        return Success(drink.copy(isFavorite = false))
    }

    private fun map(drinks: List<ApiDetailedDrink>?): Result<DetailedDrink> {
        val favorites = drinkDao.getFavorites()
        with(drinks?.first()) {
            if (this != null && idDrink != null && strDrink != null &&
                strDrinkThumb != null && strInstructions != null
            ) {
                return Success(
                    DetailedDrink(
                        idDrink,
                        strDrink,
                        strDrinkThumb,
                        strInstructions,
                        strIBA ?: "",
                        strGlass ?: "",
                        strAlcoholic == "Alcoholic",
                        buildIngredients(this),
                        favorites.any { it.id == idDrink }
                    )
                )
            } else {
                return Success(NO_DETAILS)
            }
        }
    }

    private fun buildIngredients(apiDetailedDrink: ApiDetailedDrink): List<String> {
        with(apiDetailedDrink) {
            val ingredients = listOf(
                strIngredient1, strIngredient2, strIngredient3, strIngredient4,
                strIngredient5, strIngredient6, strIngredient7, strIngredient8, strIngredient9,
                strIngredient10, strIngredient11, strIngredient12, strIngredient13, strIngredient14,
                strIngredient15
            )
            val measures = listOf(
                strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10, strMeasure11,
                strMeasure12, strMeasure13, strMeasure14, strMeasure15
            )

            return ingredients.mapIndexedNotNull { index, ingredient ->
                if (ingredient != null) {
                    if (measures[index] != null) {
                        "${measures[index]} $ingredient"
                    } else {
                        ingredient
                    }
                } else null
            }
        }
    }

    private fun drinkToDb(drink: DetailedDrink): DrinkEntity {
        return DrinkEntity(
            drink.id,
            drink.name,
            drink.thumbUrl,
            drink.instruction,
            drink.iba,
            drink.glass,
            drink.alcoholic,
            drink.ingredients
        )
    }
}