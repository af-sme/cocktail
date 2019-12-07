package de.appsfactory.cocktail.ingredient


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandora.bottomnavigator.BottomNavigator
import de.appsfactory.cocktail.R
import de.appsfactory.cocktail.drink.DrinksFilter
import de.appsfactory.cocktail.drink.DrinksFragment
import de.appsfactory.cocktail.drink.INGREDIENT
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "IngredientsFragment"

class IngredientsFragment : Fragment() {

    private val ingredientAdapter = IngredientAdapter()
    private val ingredientsViewModel: IngredientsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigator = BottomNavigator.provide(requireActivity())

        itemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        itemsRecyclerView.adapter = ingredientAdapter
        ingredientAdapter.helpClickListener =
            { _, _ -> Log.d(TAG, "helpClickListener") }
        ingredientAdapter.itemClickListener =
            { _, ingredient ->
                bottomNavigator.addFragment(
                    DrinksFragment.newInstance(DrinksFilter(INGREDIENT, ingredient.name))
                )
            }

        ingredientsViewModel.ingredients.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is NoInternetState -> showNoInternet()
                    is LoadingState -> showLoading()
                    is ErrorState -> showError(state.message)
                    is NoIngredientsState -> showNoIngredients()
                    is DisplayState -> showIngredients(state.ingredients)
                }
            }
        })

        retryButton.setOnClickListener { ingredientsViewModel.loadIngredients() }
    }

    private fun showNoInternet() {
        Log.d(TAG, "No internet")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_wifi_off_24dp)
        troubleTextView.setText(R.string.no_internet_connection)
    }

    private fun showLoading() {
        Log.d(TAG, "Loading")
        itemsRecyclerView.isVisible = false
        loading.show()
        trouble.isVisible = false
    }

    private fun showError(message: String) {
        Log.d(TAG, "Error: message")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_alert_circle_24dp)
        troubleTextView.text = message
    }

    private fun showNoIngredients() {
        Log.d(TAG, "No ingredients")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_cup_off_24dp)
        troubleTextView.setText(R.string.no_ingredients_found)
    }

    private fun showIngredients(ingredients: List<IngredientDisplayItem>) {
        Log.d(TAG, "${ingredients.size} ingredients found")
        itemsRecyclerView.isVisible = true
        loading.hide()
        trouble.isVisible = false
        ingredientAdapter.items = ingredients
    }


}
