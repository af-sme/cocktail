package de.appsfactory.cocktail.drink.details


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.fragment_drink.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val EXTRA_DRINK_ID = "extra_drink_id"

class DetailedDrinkFragment : Fragment() {

    companion object {
        fun newInstance(drinkId: String): DetailedDrinkFragment {
            val args = Bundle().apply { putString(EXTRA_DRINK_ID, drinkId) }
            return DetailedDrinkFragment().apply { arguments = args }
        }
    }

    private val detailedDrinkViewModel: DetailedDrinkViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailedDrinkViewModel.onDrinkId(
            arguments?.getString(EXTRA_DRINK_ID)
                ?: throw IllegalStateException("DrinksFilter was not provided.")
        )

        detailedDrinkViewModel.drink.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is NoInternetState -> showNoInternet()
                    is LoadingState -> showLoading()
                    is ErrorState -> showError(state.message)
                    is NoDetailsState -> showNoFlights()
                    is DisplayState -> showDrink(state.drink)
                }
            }
        })

        retryButton.setOnClickListener { detailedDrinkViewModel.loadDrink() }

        favoriteImageView.setOnClickListener { detailedDrinkViewModel.onFavoriteClick() }
    }

    private fun showNoInternet() {
        Log.d("DetailedDrinkFragment", "No internet")
        loading.hide()
        troubleGroup.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_wifi_off_24dp)
        troubleTextView.setText(R.string.no_internet_connection)
    }

    private fun showLoading() {
        Log.d("DetailedDrinkFragment", "Loading")
        drinkGroup.isVisible = false
        loading.show()
        troubleGroup.isVisible = false
    }

    private fun showError(message: String) {
        Log.d("DetailedDrinkFragment", "Error: $message")
        drinkGroup.isVisible = false
        loading.hide()
        troubleGroup.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_alert_circle_24dp)
        troubleTextView.text = message
    }

    private fun showNoFlights() {
        Log.d("DetailedDrinkFragment", "No details")
        drinkGroup.isVisible = false
        loading.hide()
        troubleGroup.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_cup_off_24dp)
        troubleTextView.setText(R.string.no_categories_found)
    }

    private fun showDrink(drink: DetailedDrinkDisplayItem) {
        Log.d("DetailedDrinkFragment", "drinks ${drink.name}")
        drinkGroup.isVisible = true
        loading.hide()
        troubleGroup.isVisible = false

        drinkTextView.text = drink.name
        detailsTextView.text = drink.details
        ingredientsTextView.text = drink.ingredients
        instructionTextView.text = drink.instruction
        drinkImageView.load(drink.thumbUrl) {
            placeholder(R.drawable.cocktail_placeholder)
            crossfade(true)
        }
        favoriteImageView.setImageResource(
            if (drink.isFavorite) {
                R.drawable.ic_favorite_24dp
            } else {
                R.drawable.ic_heart_outline_24dp
            }
        )
    }

}
