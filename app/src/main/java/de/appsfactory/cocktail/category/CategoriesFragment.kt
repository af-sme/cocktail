package de.appsfactory.cocktail.category


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
import de.appsfactory.cocktail.drink.CATEGORY
import de.appsfactory.cocktail.drink.DrinksFilter
import de.appsfactory.cocktail.drink.DrinksFragment
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {

    private val categoryAdapter = CategoryAdapter()
    private val categoriesViewModel: CategoriesViewModel by viewModel()

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
        itemsRecyclerView.adapter = categoryAdapter
        categoryAdapter.itemClickListener =
            { _, category ->
                bottomNavigator.addFragment(
                    DrinksFragment.newInstance(DrinksFilter(CATEGORY, category.name))
                )
            }

        categoriesViewModel.categories.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is NoInternetState -> showNoInternet()
                    is LoadingState -> showLoading()
                    is ErrorState -> showError(state.message)
                    is NoCategoriesState -> showNoFlights()
                    is DisplayState -> showCategories(state.categories)
                }
            }
        })

        retryButton.setOnClickListener { categoriesViewModel.loadCategories() }
    }

    private fun showNoInternet() {
        Log.d("CategoriesFragment", "No internet")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_wifi_off_24dp)
        troubleTextView.setText(R.string.no_internet_connection)
    }

    private fun showLoading() {
        Log.d("CategoriesFragment", "Loading")
        itemsRecyclerView.isVisible = false
        loading.show()
        trouble.isVisible = false
    }

    private fun showError(message: String) {
        Log.d("CategoriesFragment", "Error: $message")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_alert_circle_24dp)
        troubleTextView.text = message
    }

    private fun showNoFlights() {
        Log.d("CategoriesFragment", "No flights")
        itemsRecyclerView.isVisible = false
        loading.hide()
        trouble.isVisible = true
        troubleImageView.setImageResource(R.drawable.ic_cup_off_24dp)
        troubleTextView.setText(R.string.no_categories_found)
    }

    private fun showCategories(categories: List<CategoryDisplayItem>) {
        Log.d("CategoriesFragment", "categories ${categories.size}")
        itemsRecyclerView.isVisible = true
        loading.hide()
        trouble.isVisible = false
        categoryAdapter.items = categories
    }


}
