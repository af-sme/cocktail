package de.appsfactory.cocktail.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _login = MutableLiveData<LoginViewState>()
    val login: LiveData<LoginViewState>
        get() = _login

    fun onLogin(email: String, password: String) {
        _login.value = LoadingState
        viewModelScope.launch {
            val result = withContext(ioDispatcher) {loginUseCase.execute(email, password)}
            when (result) {
                is Success -> _login.value = SuccessLoginState
                is Error -> _login.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        Error.Reason.INVALID_CREDS -> InvalidCredsState
                        else -> {
                            Log.e("LoginViewModel", result.throwable.localizedMessage, result.throwable)
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }
}