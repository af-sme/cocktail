package de.appsfactory.cocktail

import android.Manifest
import android.widget.TextView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.azimolabs.conditionwatcher.ConditionWatcher
import de.appsfactory.cocktail.category.CategoryViewHolder
import de.appsfactory.cocktail.drink.DrinkViewHolder
import de.appsfactory.cocktail.ingredient.IngredientViewHolder
import de.appsfactory.cocktail.login.LoginActivity
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test


@LargeTest
class IngredientsScreenTest {

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun ingredientsTest() {
        //Login screen
        onView(withId(R.id.emailEditText)).perform(typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(typeText(Const.PASSWORD))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(click())

        //
        ConditionWatcher.setWatchInterval(500)
        ConditionWatcher.waitForCondition(NavigationVisible())

        //go to ingredients
        onView(withId(R.id.bottom_nav_ingredients)).perform(click())
        ConditionWatcher.waitForCondition(ItemsLoaded())
        //Categories screen
        ConditionWatcher.setWatchInterval(1000)
        ConditionWatcher.waitForCondition(ItemsLoaded())
        makeScreenshot("02_ingredients_screen")

        //Scroll to the Beer category
        onView(withId(R.id.itemsRecyclerView))
            .perform(scrollToHolder(IngredientMatcher("Bourbon")))
        makeScreenshot("03_ingredients_screen_bourbon")

        //Select shot category
        onView(withId(R.id.itemsRecyclerView))
            .perform(actionOnHolderItem(IngredientMatcher("Bourbon"), click()))
        ConditionWatcher.waitForCondition(ItemsLoaded())
        //Here we want to wait for images to be loaded
        Thread.sleep(2000)
        makeScreenshot("04_bourbon_list")

        //Scroll to and select B-52 cocktail
        onView(withId(R.id.itemsRecyclerView))
            .perform(scrollToHolder(DrinkMatcher("Old Fashioned")))
        //Here we want to wait for images to be loaded
        Thread.sleep(1500)
        makeScreenshot("05_bourbon_list_Old_Fashioned")
        onView(withId(R.id.itemsRecyclerView))
            .perform(actionOnHolderItem(DrinkMatcher("Old Fashioned"), click()))
        //Here we want to wait for image to be loaded
        Thread.sleep(1500)
        makeScreenshot("06_drink_Old_Fashioned")

        onView(withId(R.id.favoriteImageView)).perform(click())
        makeScreenshot("07_drink_Old_Fashioned")


    }

    private class IngredientMatcher(private val name: String) : TypeSafeMatcher<IngredientViewHolder>() {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(item: IngredientViewHolder?): Boolean {
            return item?.itemView?.findViewById<TextView>(R.id.ingredientTextView)?.text?.toString() == name
        }
    }

    private class DrinkMatcher(private val name: String) : TypeSafeMatcher<DrinkViewHolder>() {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(item: DrinkViewHolder?): Boolean {
            return item?.itemView?.findViewById<TextView>(R.id.drinkTextView)?.text?.toString() == name
        }
    }
}