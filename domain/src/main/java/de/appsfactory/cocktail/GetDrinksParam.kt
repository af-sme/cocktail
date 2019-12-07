package de.appsfactory.cocktail

class GetDrinksParam(val type: Type, val value: String = "") {
    enum class Type { CATEGORY, INGREDIENT, FAVORITE }
}
