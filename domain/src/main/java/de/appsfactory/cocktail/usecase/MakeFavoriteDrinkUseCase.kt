package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.DetailedDrink
import de.appsfactory.cocktail.repository.DetailedDrinkRepository

class MakeFavoriteDrinkUseCase(private val detailedDrinkRepository: DetailedDrinkRepository) {
    fun execute(isFavorite: Boolean, drink: DetailedDrink): Result<DetailedDrink> {
        return if (isFavorite) {
            detailedDrinkRepository.addFavoriteDrink(drink)
        } else {
            detailedDrinkRepository.removeFavoriteDrink(drink)
        }
    }
}