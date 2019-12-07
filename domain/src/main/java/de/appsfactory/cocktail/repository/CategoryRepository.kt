package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Category

interface CategoryRepository {
    fun getCategories(): Result<List<Category>>
}