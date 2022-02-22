package com.example.composebudgetapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composebudgetapp.R


enum class AppScreen(val icon: ImageVector, val navigation: Int) {
    Overview(Icons.Filled.PieChart, R.id.overviewDestination),
    Accounts(Icons.Filled.AttachMoney, R.id.accountsDestination),
    Bills(Icons.Filled.MoneyOff, R.id.billsDestination);

    companion object {
        fun valueOf(value: Int): AppScreen{
            return AppScreen.values().find {
                it.navigation == value
            }!!
        }
    }
}