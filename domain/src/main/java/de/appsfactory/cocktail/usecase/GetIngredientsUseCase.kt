package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Ingredient
import de.appsfactory.cocktail.repository.IngredientRepository

class GetIngredientsUseCase(private val ingredientRepository: IngredientRepository) {
    fun execute(): Result<List<Ingredient>> {
        return ingredientRepository.getIngredients()
    }
}