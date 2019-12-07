package de.appsfactory.cocktail.drink


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pandora.bottomnavigator.BottomNavigator
import de.appsfactory.cocktail.R
import de.appsfactory.cocktail.drink.details.DetailedDrinkFragment
import de.appsfactory.cocktail.shared.toPx
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val EXTRA_MODE = "extra_mode"

private const val TAG = "DrinksFragment"

class DrinksFragment : Fragment() {

    companion object {
        fun newInstance(drinksFilter: DrinksFilter): DrinksFragment {
            val args = Bundle().apply { putParcelable(EXTRA_MODE, drinksFilter) }
            return DrinksFragment().apply { arguments = args }
        }
    }

    private val drinksFilter: DrinksFilter by lazy {
        arguments?.getParcelable<DrinksFilter>(EXTRA_MODE)
            ?: throw IllegalStateException("DrinksFilter was not provided.")
    }

    private val drinkAdapter = DrinkAdapter()
    private val drinksViewModel: DrinksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigator = BottomNavigator.provide(requireActivity())

        drinksViewModel.onModeDefined(drinksFilter)

        itemsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        itemsRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, 8.toPx))
        itemsRecyclerView.adapter = drinkAdapter
        drinkAdapter.itemClickListener =
            { _, drink ->
                bottomNavigator.addFragment(DetailedDrinkFragment.newInstance(drink.id))}

        drinksViewModel.drinks.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is NoInternetState -> showNoInternet()
                    is LoadingState -> showLoading()
                    is ErrorState -> showError(state.message)
                    is NoDrinksState -> showNoDrinks()
                    is DisplayState -> showDrinks(state.drinks)
                }
            }
        })

        retryButton.setOnClickListener { drinksViewModel.loadDrinks() }
    }

    override fun onResume() {
        super.onResume()
        drinksViewModel.onResume()
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
        Log.d(TAG, "Error: $message")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_alert_circle_24dp)
        troubleTextView.text = message
    }

    private fun showNoDrinks() {
        Log.d(TAG, "No drinks")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_cup_off_24dp)
        troubleTextView.setText(R.string.no_drinks_found)
    }

    private fun showDrinks(drinks: List<DrinkDisplayItem>) {
        Log.d(TAG, "${drinks.size} drinks found")
        itemsRecyclerView.isVisible = true
        loading.hide()
        trouble.isVisible = false
        drinkAdapter.items = drinks
    }


}
