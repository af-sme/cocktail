package de.appsfactory.cocktail.rest.model

class ApiDrinks(
	val drinks: List<ApiDrink>?
)

class ApiDrink(
	val strDrink: String?,
	val strDrinkThumb: String?,
	val idDrink: String?
)
