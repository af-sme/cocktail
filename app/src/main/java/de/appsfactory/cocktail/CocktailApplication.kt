package de.appsfactory.cocktail

import android.app.Application
import de.appsfactory.cocktail.di.appModule
import de.appsfactory.cocktail.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CocktailApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CocktailApplication)
            modules(listOf(appModule, dataModule))
        }
    }
}