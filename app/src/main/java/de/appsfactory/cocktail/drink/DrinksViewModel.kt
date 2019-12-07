package de.appsfactory.cocktail.drink

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.GetDrinksParam
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.usecase.GetDrinksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrinksViewModel(private val getDrinksUseCase: GetDrinksUseCase) : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _drinks = MutableLiveData<DrinksViewState>()
    val drinks: LiveData<DrinksViewState> by lazy {
        loadDrinks()
        _drinks
    }

    private lateinit var getDrinksParam: GetDrinksParam

    //TODO probable proper way to update favorites is to pass LiveData from DrinkDao
    internal fun onResume() {
        if (::getDrinksParam.isInitialized && getDrinksParam.type == GetDrinksParam.Type.FAVORITE) {
            loadDrinks()
        }
    }

    internal fun onModeDefined(drinksFilter: DrinksFilter) {
        getDrinksParam = when (drinksFilter.type) {
            CATEGORY -> GetDrinksParam(GetDrinksParam.Type.CATEGORY, drinksFilter.filter)
            INGREDIENT -> GetDrinksParam(GetDrinksParam.Type.INGREDIENT, drinksFilter.filter)
            else -> GetDrinksParam(GetDrinksParam.Type.FAVORITE)
        }
    }

    internal fun loadDrinks() {
        _drinks.value = LoadingState
        viewModelScope.launch {
            when (val result =
                withContext(ioDispatcher) { getDrinksUseCase.execute(getDrinksParam) }) {
                is Success -> _drinks.value =
                    if (result.value.isNotEmpty()) {
                        DisplayState(result.value
                            .map { DrinkDisplayItem(it.id, it.name, it.thumbUrl) })
                    } else {
                        NoDrinksState
                    }
                is Error -> _drinks.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        else -> {
                            Log.e(
                                "DrinksViewModel",
                                result.throwable.localizedMessage,
                                result.throwable
                            )
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }
}