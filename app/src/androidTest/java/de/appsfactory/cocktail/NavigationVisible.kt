package de.appsfactory.cocktail

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.azimolabs.conditionwatcher.Instruction
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationVisible : Instruction() {
    override fun getDescription(): String {
        return "Navigation visible"
    }

    override fun checkCondition(): Boolean {
        val bottomNavigationView =
            getCurrentActivity()
                ?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        return bottomNavigationView != null && bottomNavigationView.isVisible
    }
}