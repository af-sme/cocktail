package de.appsfactory.cocktail

import android.Manifest
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.azimolabs.conditionwatcher.ConditionWatcher
import com.azimolabs.conditionwatcher.Instruction
import de.appsfactory.cocktail.login.LoginActivity
import org.junit.Rule
import org.junit.Test

@LargeTest
class LoginScreenTest {

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
    fun openLoginScreenTest() {
        onView(withId(R.id.emailEditText)).perform(typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(typeText(Const.PASSWORD))
        closeSoftKeyboard()

        makeScreenshot("openLoginScreenTest")
    }

    @Test
    fun successfulLoginTest() {
        onView(withId(R.id.emailEditText)).perform(typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(typeText(Const.PASSWORD))
        closeSoftKeyboard()

        onView(withId(R.id.loginButton)).perform(ViewActions.click())

        ConditionWatcher.setWatchInterval(1000)
        ConditionWatcher.waitForCondition(ItemsLoaded())

        makeScreenshot("successfulLoginTest")
    }

    private class ItemsLoaded : Instruction() {
        override fun getDescription(): String {
            return "Items loaded"
        }

        override fun checkCondition(): Boolean {
            val itemsRecyclerView =
                getCurrentActivity()?.findViewById<RecyclerView>(R.id.itemsRecyclerView)
            return itemsRecyclerView != null && itemsRecyclerView.childCount > 5
        }
    }
}