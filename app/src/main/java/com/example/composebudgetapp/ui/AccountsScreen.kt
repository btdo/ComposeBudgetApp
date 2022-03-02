package com.example.composebudgetapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.FakeData


@Preview
@Composable
fun AccountsScreenPreview(){
    AccountsScreen(FakeData.accountList, {})
}

@Composable
fun AccountsScreen(accounts: List<Account>, onAccountSelected: (Account) -> Unit){
    Column {
        AccountsTotal(accounts)
        accounts.forEach {
            AccountItem(account = it) {
                onAccountSelected(it)
            }
            AppDivider()
        }
    }
}


@Composable
fun AccountsTotal(accounts: List<Account>) {
    ItemsTotal(items = accounts, getColor = {
        it.color
    }, getValue = {
        it.balance
    } )
}






