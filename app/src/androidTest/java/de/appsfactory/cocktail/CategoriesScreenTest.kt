package de.appsfactory.cocktail

import android.Manifest
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.azimolabs.conditionwatcher.Instruction
import de.appsfactory.cocktail.category.CategoryAdapter
import de.appsfactory.cocktail.category.CategoryViewHolder
import de.appsfactory.cocktail.drink.DrinkAdapter
import de.appsfactory.cocktail.drink.DrinkViewHolder
import de.appsfactory.cocktail.login.LoginActivity
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test


@LargeTest
class CategoriesScreenTest {

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
    fun categoriesTest() {
        //Login screen
        onView(withId(R.id.emailEditText)).perform(typeText(Const.EMAIL))
        onView(withId(R.id.passwordEditText)).perform(typeText(Const.PASSWORD))
        closeSoftKeyboard()
        makeScreenshot("01_login_screen")

        onView(withId(R.id.loginButton)).perform(click())

        //Categories screen
        ConditionWatcher.setWatchInterval(1000)
        ConditionWatcher.waitForCondition(ItemsLoaded())
        makeScreenshot("02_categories_screen")

        //Scroll to the Beer category
        onView(withId(R.id.itemsRecyclerView))
            .perform(scrollToHolder(CategoryMatcher("Beer")))
        ConditionWatcher.waitForCondition(CategoryVisible("Beer"))
        makeScreenshot("03_categories_screen_bier")

        //Select shot category
        onView(withId(R.id.itemsRecyclerView))
            .perform(actionOnHolderItem(CategoryMatcher("Shot"), click()))
        //Here we want to wait for images to be loaded
        Thread.sleep(2000)
        makeScreenshot("04_shots_list")

        //Scroll to and select B-52 cocktail
        onView(withId(R.id.itemsRecyclerView))
            .perform(scrollToHolder(DrinkMatcher("B-52")))
        ConditionWatcher.waitForCondition(DrinkVisible("B-52"))
        makeScreenshot("05_shots_list_b-52")
        onView(withId(R.id.itemsRecyclerView))
            .perform(actionOnHolderItem(DrinkMatcher("B-52"), click()))
        //Here we want to wait for image to be loaded
        Thread.sleep(1500)
        makeScreenshot("06_drink_b-52")

        onView(withId(R.id.favoriteImageView)).perform(click())
        makeScreenshot("07_drink_b-52")


    }

    private class CategoryVisible(private val name: String) : Instruction() {
        override fun getDescription(): String {
            return "Category $name visible"
        }

        override fun checkCondition(): Boolean {
            val itemsRecyclerView =
                getCurrentActivity()?.findViewById<RecyclerView>(R.id.itemsRecyclerView)
            return itemsRecyclerView != null && itemsRecyclerView.childCount > 0
                    && (itemsRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition().let {
                (itemsRecyclerView.adapter as CategoryAdapter).items[it].name == name
            }
        }
    }

    private class CategoryMatcher(private val name: String) : TypeSafeMatcher<CategoryViewHolder>() {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(item: CategoryViewHolder?): Boolean {
            return item?.itemView?.findViewById<TextView>(R.id.categoryTextView)?.text?.toString() == name
        }
    }

    private class DrinkVisible(private val name: String) : Instruction() {
        override fun getDescription(): String {
            return "Drink $name visible"
        }

        override fun checkCondition(): Boolean {
            val itemsRecyclerView =
                getCurrentActivity()?.findViewById<RecyclerView>(R.id.itemsRecyclerView)
            return itemsRecyclerView != null && itemsRecyclerView.childCount > 0
                    && (itemsRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition().let {
                (itemsRecyclerView.adapter as DrinkAdapter).items[it].name == name
            }
        }
    }

    private class DrinkMatcher(private val name: String) : TypeSafeMatcher<DrinkViewHolder>() {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(item: DrinkViewHolder?): Boolean {
            return item?.itemView?.findViewById<TextView>(R.id.drinkTextView)?.text?.toString() == name
        }
    }
}