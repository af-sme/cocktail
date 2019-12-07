package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.DetailedDrink
import de.appsfactory.cocktail.repository.DetailedDrinkRepository

class GetDetailedDrinkUseCase(val detailedDrinkRepository: DetailedDrinkRepository) {

    fun execute(id: String): Result<DetailedDrink> {
        return detailedDrinkRepository.getDetailedDrink(id)
    }
}