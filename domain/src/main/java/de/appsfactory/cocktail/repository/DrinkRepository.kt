package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Drink

interface DrinkRepository {
    fun getDrinksByCategory(categoryName: String): Result<List<Drink>>
    fun getDrinksByIngredient(ingredientName: String): Result<List<Drink>>
    fun getFavoriteDrinks(): Result<List<Drink>>
}
