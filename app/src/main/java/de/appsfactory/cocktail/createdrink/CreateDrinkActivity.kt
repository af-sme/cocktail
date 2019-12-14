package de.appsfactory.cocktail.createdrink

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.activity_create_drink.*

/**
 * This activity has additional purpose: another task for candidates.
 * It created as god-class purposely.
 */
class CreateDrinkActivity : AppCompatActivity() {

    private var selectedCategoryIndex = -1
    private val selectedIngredients by lazy {
        BooleanArray(resources.getStringArray(R.array.ingredients_array).size) { false }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_drink)

        categoryTextView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setSingleChoiceItems(R.array.categories_array, selectedCategoryIndex)
            { _, which ->
                selectedCategoryIndex = which
                categoryTextView.text = resources.getStringArray(R.array.categories_array)[which]
            }
            builder.setNeutralButton(R.string.close) { _, _ -> }
            builder.create().show()
        }

        ingredientsTextView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMultiChoiceItems(
                R.array.ingredients_array,
                selectedIngredients
            )
            { _, which, isChecked ->
                selectedIngredients[which] = isChecked
                ingredientsTextView.text = resources.getStringArray(R.array.ingredients_array)
                    .filterIndexed { index, _ -> selectedIngredients[index] }.joinToString()
            }
            builder.setNeutralButton(R.string.close) { _, _ -> }
            builder.create().show()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.glasses_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            glassSpinner.adapter = adapter
        }

        button.setOnClickListener { finish() }
    }


}
