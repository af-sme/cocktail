package de.appsfactory.cocktail.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.google.gson.GsonBuilder
import de.appsfactory.cocktail.BuildConfig
import de.appsfactory.cocktail.Const
import de.appsfactory.cocktail.connectivity.Connectivity
import de.appsfactory.cocktail.db.CocktailDatabase
import de.appsfactory.cocktail.repository.*
import de.appsfactory.cocktail.rest.TheCocktailDBService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<CategoryRepository> { CategoryRemoteRepository(get(), get()) }
    single<IngredientRepository> { IngredientRemoteRepository(get(), get()) }
    single<DrinkRepository> { DrinkComposeRepository(get(), get(), get()) }
    single<DetailedDrinkRepository> { DetailedDrinkComposeRepository(get(), get(), get()) }
    single { provideRestService() }
    single { provideConnectivity(androidContext()) }
    single { provideDatabase(androidContext()) }
    single { get<CocktailDatabase>().drinkDao() }
}

private fun provideRestService(): TheCocktailDBService {
    return provideRetrofit().create(TheCocktailDBService::class.java)
}

private fun provideRetrofit(): Retrofit {
    val gson = GsonBuilder()
        .create()

    val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }.build()

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Const.COCKTAIL_API_URL)
        .client(client)
        .build()
}

//TODO rid of deprecation using {@link android.net.ConnectivityManager.NetworkCallback} API
fun provideConnectivity(context: Context): Connectivity {
    return object : Connectivity {
        override fun isConnectedToInternet(): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting ?: false
        }
    }
}

private fun provideDatabase(context: Context): CocktailDatabase {
    return Room.databaseBuilder(
        context,
        CocktailDatabase::class.java, "cocktails"
    ).build()
}