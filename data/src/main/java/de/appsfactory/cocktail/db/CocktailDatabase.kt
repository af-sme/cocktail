package de.appsfactory.cocktail.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.appsfactory.cocktail.db.model.DrinkEntity
import de.appsfactory.cocktail.db.model.IngredientsConverter

@Database(entities = [DrinkEntity::class], version = 1)
@TypeConverters(IngredientsConverter::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}