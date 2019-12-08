package de.appsfactory.cocktail.usecase

import de.appsfactory.cocktail.Result
import de.appsfactory.cocktail.repository.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {
    fun execute(email: String, password: String): Result<Any> {
        return loginRepository.login(email, password)
    }
}