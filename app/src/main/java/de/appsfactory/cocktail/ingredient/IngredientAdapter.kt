package de.appsfactory.cocktail.ingredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.item_ingredient.view.*


class IngredientAdapter : RecyclerView.Adapter<IngredientViewHolder>(),
    IngredientViewHolder.ItemClickListener, IngredientViewHolder.HelpClickListener {

    var items: List<IngredientDisplayItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((Int, IngredientDisplayItem) -> Unit)? = null

    var helpClickListener: ((Int, IngredientDisplayItem) -> Unit)? = null

    override fun onItemClick(position: Int) {
        itemClickListener?.invoke(position, items[position])
    }

    override fun onHelpClick(position: Int) {
        helpClickListener?.invoke(position, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(itemView, this, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            ingredientAbbrTextView.text = item.abbr
            ingredientTextView.text = item.name
        }
    }
}

open class IngredientViewHolder(itemView: View,
                                itemClickListener: ItemClickListener? = null,
                                helpClickListener: HelpClickListener? = null) :
    RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener { itemClickListener?.onItemClick(adapterPosition) }
        itemView.helpImageView.setOnClickListener { helpClickListener?.onHelpClick(adapterPosition) }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    interface HelpClickListener {
        fun onHelpClick(position: Int)
    }

}