package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Const
import de.appsfactory.cocktail.Error
import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.Success
import de.appsfactory.cocktail.connectivity.Connectivity
import de.appsfactory.cocktail.entity.Login

class LoginRemoteRepository(private val connectivity: Connectivity) : LoginRepository {

    override fun login(email: String, password: String): Result<Login> {
        Thread.sleep(2000)
        return if (connectivity.isConnectedToInternet()) {
            if (email == Const.EMAIL && password == Const.PASSWORD) {
                Success(Login("1"))
            } else {
                Error(Error.Reason.INVALID_CREDS, Throwable("Invalid credentials"))
            }
        } else {
            Error(Error.Reason.NO_INTERNET, Throwable("No internet connection"))
        }
    }
}