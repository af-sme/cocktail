package de.appsfactory.cocktail.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.appsfactory.cocktail.R
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter: RecyclerView.Adapter<CategoryViewHolder>(), ((Int) -> Unit) {

    var items: List<CategoryDisplayItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((Int, CategoryDisplayItem) -> Unit)? = null

    override fun invoke(position: Int) {
        itemClickListener?.invoke(position, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(itemView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            categoryImageView.setImageResource(item.icon)
            categoryTextView.text = item.name
        }
    }
}

open class CategoryViewHolder(itemView: View, clickListener: ((Int) -> Unit)? = null) :
    RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener { clickListener?.invoke(adapterPosition) }
    }
}