package de.appsfactory.cocktail.drink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.item_drink.view.*


class DrinkAdapter : RecyclerView.Adapter<DrinkViewHolder>(), ((Int) -> Unit) {

    var items: List<DrinkDisplayItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((Int, DrinkDisplayItem) -> Unit)? = null

    override fun invoke(position: Int) {
        itemClickListener?.invoke(position, items[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_drink, parent, false)
        return DrinkViewHolder(itemView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            drinkTextView.text = item.name
            drinkImageView.load(item.thumbUrl) {
                placeholder(R.drawable.cocktail_placeholder)
                crossfade(true)
            }
        }
    }
}

open class DrinkViewHolder(itemView: View,
                           clickListener: ((Int) -> Unit)? = null) :
    RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener { clickListener?.invoke(adapterPosition) }
    }
}