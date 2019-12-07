package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Category
import de.appsfactory.cocktail.repository.CategoryRepository

class GetCategoriesUseCase(private val categoryRepository: CategoryRepository) {
    fun execute(): Result<List<Category>> {
        return categoryRepository.getCategories()
    }
}