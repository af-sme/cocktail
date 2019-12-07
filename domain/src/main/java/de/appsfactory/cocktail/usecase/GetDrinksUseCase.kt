package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.GetDrinksParam
import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Drink
import de.appsfactory.cocktail.repository.DrinkRepository

class GetDrinksUseCase(private val drinkRepository: DrinkRepository) {

    fun execute(getDrinksParam: GetDrinksParam): Result<List<Drink>> {
        return when (getDrinksParam.type) {
            GetDrinksParam.Type.CATEGORY -> drinkRepository.getDrinksByCategory(getDrinksParam.value)
            GetDrinksParam.Type.INGREDIENT -> drinkRepository.getDrinksByIngredient(getDrinksParam.value)
            else -> drinkRepository.getFavoriteDrinks()
        }
    }
}