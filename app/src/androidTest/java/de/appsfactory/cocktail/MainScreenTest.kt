package de.appsfactory.cocktail

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.azimolabs.conditionwatcher.ConditionWatcher
import de.appsfactory.cocktail.login.LoginActivity
import org.junit.Rule
import org.junit.Test


@LargeTest
class MainScreenTest {
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun highLevelNavigation() {
        //Login screen
        onView(withId(R.id.emailEditText)).perform(ViewActions.typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText(Const.PASSWORD))
        Espresso.closeSoftKeyboard()

        //openMainScreen
        onView(withId(R.id.loginButton)).perform(click())

        ConditionWatcher.setWatchInterval(1000)
        ConditionWatcher.waitForCondition(ItemsLoaded())

        //categories loaded
        makeScreenshot("highLevelNavigation_categories")

        //go to ingredients
        onView(withId(R.id.bottom_nav_ingredients)).perform(click())
        ConditionWatcher.waitForCondition(ItemsLoaded())
        makeScreenshot("highLevelNavigation_ingredients_1")

        //go to favorites
        onView(withId(R.id.bottom_nav_favorites)).perform(click())
        //favorites stored locally, so will be loaded pretty fast
        Thread.sleep(500)
        makeScreenshot("highLevelNavigation_favorites")

        //should open already loaded ingredients
        pressBack()
        makeScreenshot("highLevelNavigation_ingredients_2")

        //should open already loaded categories
        pressBack()
        makeScreenshot("highLevelNavigation_categories_2")
    }
}