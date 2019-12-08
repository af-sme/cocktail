package de.appsfactory.cocktail.di

import de.appsfactory.cocktail.category.CategoriesViewModel
import de.appsfactory.cocktail.drink.DrinksViewModel
import de.appsfactory.cocktail.drink.details.DetailedDrinkViewModel
import de.appsfactory.cocktail.ingredient.IngredientsViewModel
import de.appsfactory.cocktail.login.LoginViewModel
import de.appsfactory.cocktail.usecase.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { LoginViewModel(get()) }

    factory { LoginUseCase(get()) }

    viewModel { CategoriesViewModel(get()) }

    factory { GetCategoriesUseCase(get()) }

    viewModel { IngredientsViewModel(get()) }

    factory { GetIngredientsUseCase(get()) }

    viewModel { DrinksViewModel(get()) }

    factory { GetDrinksUseCase(get()) }

    viewModel { DetailedDrinkViewModel(get(), get()) }

    factory { GetDetailedDrinkUseCase(get()) }

    factory { MakeFavoriteDrinkUseCase(get()) }
}