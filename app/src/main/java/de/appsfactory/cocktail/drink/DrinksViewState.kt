package de.appsfactory.cocktail.drink

sealed class DrinksViewState
object NoInternetState : DrinksViewState()
object LoadingState : DrinksViewState()
class ErrorState(val message: String): DrinksViewState()
object NoDrinksState : DrinksViewState()
class DisplayState(val drinks: List<DrinkDisplayItem>): DrinksViewState()