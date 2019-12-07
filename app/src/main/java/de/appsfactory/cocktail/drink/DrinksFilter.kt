package de.appsfactory.cocktail.drink

import android.os.Parcelable
import androidx.annotation.IntDef
import kotlinx.android.parcel.Parcelize

const val CATEGORY = 0
const val INGREDIENT = 1
const val FAVORITE = 2

@Retention(AnnotationRetention.SOURCE)
@IntDef(CATEGORY, INGREDIENT, FAVORITE)
annotation class FilterType

@Parcelize
class DrinksFilter(@FilterType val type: Int, val filter: String): Parcelable