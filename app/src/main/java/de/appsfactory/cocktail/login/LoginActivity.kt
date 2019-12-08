package de.appsfactory.cocktail.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import de.appsfactory.cocktail.MainActivity
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            loginViewModel.onLogin(
                emailEditText.text.toString(), passwordEditText.text.toString()
            )
        }

        loginViewModel.login.observe(this, Observer {
            when (it) {
                is LoadingState -> showLoading()
                is NoInternetState -> showNoInternet()
                is InvalidCredsState -> showInvalidCreds()
                is ErrorState -> showError(it.message)
                is SuccessLoginState -> showMainScreen()
            }
        })
    }

    private fun showLoading() {
        loading.show()
        errorTextView.isVisible = false
    }

    private fun showNoInternet() {
        loading.hide()
        errorTextView.isVisible = true
        errorTextView.setText(R.string.no_internet_connection)
    }

    private fun showInvalidCreds() {
        loading.hide()
        errorTextView.isVisible = true
        errorTextView.setText(R.string.invalid_creds)
    }

    private fun showError(message: String) {
        loading.hide()
        errorTextView.isVisible = true
        errorTextView.text = message
    }

    private fun showMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
