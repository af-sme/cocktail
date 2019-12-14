package de.appsfactory.cocktail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pandora.bottomnavigator.BottomNavigator
import de.appsfactory.cocktail.category.CategoriesFragment
import de.appsfactory.cocktail.createdrink.CreateDrinkActivity
import de.appsfactory.cocktail.drink.DrinksFilter
import de.appsfactory.cocktail.drink.DrinksFragment
import de.appsfactory.cocktail.drink.FAVORITE
import de.appsfactory.cocktail.ingredient.IngredientsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var navigator: BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navigator = BottomNavigator.onCreate(
            fragmentContainer = R.id.fragmentContainer,
            bottomNavigationView = findViewById(R.id.bottomNavigationView),
            rootFragmentsFactory = mapOf(
                R.id.bottom_nav_categories to { CategoriesFragment() },
                R.id.bottom_nav_ingredients to { IngredientsFragment() },
                R.id.bottom_nav_favorites to {
                    DrinksFragment.newInstance(
                        DrinksFilter(
                            FAVORITE,
                            ""
                        )
                    )
                }
            ),
            defaultTab = R.id.bottom_nav_categories,
            activity = this
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_cocktail) {
            val intent = Intent(this, CreateDrinkActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!navigator.pop()) {
            super.onBackPressed()
        }
    }
}
