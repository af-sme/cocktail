package de.appsfactory.cocktail

import androidx.recyclerview.widget.RecyclerView
import com.azimolabs.conditionwatcher.Instruction

class ItemsLoaded : Instruction() {
    override fun getDescription(): String {
        return "Items loaded"
    }

    override fun checkCondition(): Boolean {
        val itemsRecyclerView =
            getCurrentActivity()
                ?.findViewById<RecyclerView>(R.id.itemsRecyclerView)
        return itemsRecyclerView != null && itemsRecyclerView.childCount > 5
    }
}