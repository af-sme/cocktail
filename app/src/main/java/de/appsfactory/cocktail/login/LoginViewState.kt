package de.appsfactory.cocktail.login

sealed class LoginViewState
object NoInternetState : LoginViewState()
object LoadingState : LoginViewState()
object InvalidCredsState: LoginViewState()
class ErrorState(val message: String): LoginViewState()
object SuccessLoginState: LoginViewState()