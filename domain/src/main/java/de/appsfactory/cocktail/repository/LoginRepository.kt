package de.appsfactory.cocktail.repository

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.entity.Login

interface LoginRepository {
    fun login(email: String, password: String): Result<Login>
}