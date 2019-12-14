package de.appsfactory.cocktail

import android.Manifest
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.azimolabs.conditionwatcher.ConditionWatcher
import de.appsfactory.cocktail.login.LoginActivity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test

@LargeTest
class CreateDrinkScreenTest {

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
    fun createDrinkTest() {
        onView(withId(R.id.emailEditText)).perform(typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(typeText(Const.PASSWORD))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(click())

        ConditionWatcher.waitForCondition(ItemsLoaded())

        //click on menu item
        onView(withId(R.id.menu_add_cocktail)).perform(click())

        //type cocktail name
        onView(withId(R.id.nameEditText)).perform(typeText("Margarita"))

        //type cocktail recipe
        onView(withId(R.id.descriptionEditText)).perform(typeText("Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass."))

        closeSoftKeyboard()

        //open categories
        onView(withId(R.id.categoryTextView)).perform(click())

        //select category
        onData(equalTo("Ordinary Drink")).perform(click())

        //close categories
        onView(withText(R.string.close)).perform(click())

        //open ingredients
        onView(withId(R.id.ingredientsTextView)).perform(click())

        //select ingredient
        onData(equalTo("Tequila")).perform(click())

        //select ingredient
        onData(equalTo("Triple sec")).perform(click())

        //select ingredient
        onData(equalTo("Lime juice")).perform(click())

        //select ingredient
        onData(equalTo("Salt")).perform(click())

        //close ingredients
        onView(withText(R.string.close)).perform(click())

        //open glasses spinner
        onView(withId(R.id.glassSpinner)).perform(click())

        //select glass
        onData(equalTo("Margarita glass")).perform(click())

        //make cocktail alcoholic
        onView(withId(R.id.alcoholicSwitch)).perform(click())

        makeScreenshot("create_drink")

        onView(withId(R.id.button)).perform(click())
    }
}