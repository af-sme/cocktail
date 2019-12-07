package de.appsfactory.cocktail.drink.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.entity.DetailedDrink
import de.appsfactory.cocktail.usecase.GetDetailedDrinkUseCase
import de.appsfactory.cocktail.usecase.MakeFavoriteDrinkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedDrinkViewModel(
    private val getDetailedDrinkUseCase: GetDetailedDrinkUseCase,
    private val makeFavoriteDrinkUseCase: MakeFavoriteDrinkUseCase
) :
    ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _drink = MutableLiveData<DetailedDrinkViewState>()
    val drink: LiveData<DetailedDrinkViewState> by lazy {
        loadDrink()
        _drink
    }

    private lateinit var drinkId: String
    private lateinit var currentDrink: DetailedDrink

    internal fun onDrinkId(drinkId: String) {
        this.drinkId = drinkId
    }

    internal fun loadDrink() {
        _drink.value = LoadingState
        viewModelScope.launch {
            when (val result =
                withContext(ioDispatcher) { getDetailedDrinkUseCase.execute(drinkId) }) {
                is Success ->
                    if (result.value != DetailedDrink.NO_DETAILS) {
                        currentDrink = result.value
                        _drink.value = DisplayState(toDisplayItem(result.value))
                    } else {
                        _drink.value = NoDetailsState
                    }
                is Error -> _drink.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        else -> {
                            Log.e(
                                "DetailedDrinkViewModel",
                                result.throwable.localizedMessage,
                                result.throwable
                            )
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }

    fun onFavoriteClick() {
        if (::currentDrink.isInitialized) {
            val isFavorite = currentDrink.isFavorite
            viewModelScope.launch {
                val drinkResult = withContext(ioDispatcher) {
                    makeFavoriteDrinkUseCase.execute(!isFavorite, currentDrink)
                }
                if (drinkResult is Success) {
                    _drink.value = DisplayState(toDisplayItem(drinkResult.value))
                }
            }
        }
    }

    private fun toDisplayItem(value: DetailedDrink): DetailedDrinkDisplayItem {
        return with(value) {
            DetailedDrinkDisplayItem(
                name = name,
                thumbUrl = thumbUrl,
                details = listOf(iba, if (alcoholic) "Alcoholic" else "Non alcoholic", glass)
                    .filter { it.isNotBlank() }.joinToString("\n"),
                ingredients = ingredients.joinToString("\n"),
                instruction = instruction,
                isFavorite = isFavorite
            )
        }
    }
}