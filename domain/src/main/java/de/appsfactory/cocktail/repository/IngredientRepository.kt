package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Ingredient

interface IngredientRepository {
    fun getIngredients(): Result<List<Ingredient>>
}
