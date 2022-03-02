package com.example.composebudgetapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composebudgetapp.data.Bill

@Composable
fun BillsScreen(bills: List<Bill>, onBillSelected: (Bill) -> Unit) {
    Column {
        BillsTotal(bills)
        bills.forEach {
            BillItem(it) {
                onBillSelected(it)
            }
        }
    }
}

@Composable
fun BillsTotal(bills: List<Bill>){
    ItemsTotal(items =  bills, getColor = {
        it.color
    }, getValue = {
        it.amount
    })
}