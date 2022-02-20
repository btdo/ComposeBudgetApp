package com.example.composebudgetapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        ItemsTotal(accounts)
        accounts.forEach {
            AccountItem(account = it, modifier = Modifier.clickable {
                onAccountSelected(it)
            })
            AppDivider()
        }
    }
}







