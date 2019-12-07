package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.DetailedDrink

interface DetailedDrinkRepository {
    fun getDetailedDrink(id: String): Result<DetailedDrink>
    fun addFavoriteDrink(drink: DetailedDrink): Result<DetailedDrink>
    fun removeFavoriteDrink(drink: DetailedDrink): Result<DetailedDrink>
}