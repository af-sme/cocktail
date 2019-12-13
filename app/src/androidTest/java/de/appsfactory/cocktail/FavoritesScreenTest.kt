package de.appsfactory.cocktail

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.azimolabs.conditionwatcher.ConditionWatcher
import de.appsfactory.cocktail.category.CategoryViewHolder
import de.appsfactory.cocktail.drink.DrinkViewHolder
import de.appsfactory.cocktail.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@LargeTest
class FavoritesScreenTest {

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
    fun favoritesTest() {
        //Login screen
        onView(withId(R.id.emailEditText)).perform(ViewActions.typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText(Const.PASSWORD))
        closeSoftKeyboard()

        //openMainScreen
        onView(withId(R.id.loginButton)).perform(click())

        ConditionWatcher.setWatchInterval(1000)
        ConditionWatcher.waitForCondition(ItemsLoaded())

        //add some random favorites
        val itemsRecyclerView =
            getCurrentActivity()!!.findViewById<RecyclerView>(R.id.itemsRecyclerView)
        val childCount = itemsRecyclerView.childCount
        val randomPositions = List(3) { Random.nextInt(0, childCount) }

        randomPositions.forEach { randomPos ->
            //categories
            onView(withId(R.id.itemsRecyclerView))
                .perform(
                    scrollToPosition<CategoryViewHolder>(randomPos)
                )
            onView(withId(R.id.itemsRecyclerView))
                .perform(
                    actionOnItemAtPosition<CategoryViewHolder>(randomPos, click())
                )

            //wait for drinks
            ConditionWatcher.waitForCondition(ItemsLoaded())

            val drinksRecyclerView =
                getCurrentActivity()!!.findViewById<RecyclerView>(R.id.itemsRecyclerView)
            val drinksCount = drinksRecyclerView.childCount

            //open random drink
            val pos = Random.nextInt(0, drinksCount)
            onView(withId(R.id.itemsRecyclerView))
                .perform(
                    scrollToPosition<DrinkViewHolder>(pos)
                )
            onView(withId(R.id.itemsRecyclerView))
                .perform(
                    actionOnItemAtPosition<DrinkViewHolder>(pos, click())
                )
            Thread.sleep(1500)

            //make that drink favorite
            onView(withId(R.id.favoriteImageView)).perform(click())
            Thread.sleep(250)

            //back to categories
            onView(withId(R.id.bottom_nav_categories)).perform(click())
            ConditionWatcher.waitForCondition(ItemsLoaded())
        }

        onView(withId(R.id.bottom_nav_favorites)).perform(click())
        Thread.sleep(500)
        makeScreenshot("01_favorites")

        onView(withId(R.id.itemsRecyclerView))
            .perform(
                actionOnItemAtPosition<DrinkViewHolder>(0, click())
            )
        Thread.sleep(1500)
        makeScreenshot("02_favorite_drink")

        //make that drink not favorite
        onView(withId(R.id.favoriteImageView)).perform(click())
        Thread.sleep(250)
        makeScreenshot("03_not_favorite_drink")
        pressBack()
        Thread.sleep(250)
        //see, that drink is not here anymore
        makeScreenshot("04_favorites")
    }
}