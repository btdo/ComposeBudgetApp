package com.example.composebudgetapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import java.text.DecimalFormat

val RallyDefaultPadding = 12.dp
val AccountDecimalFormat = DecimalFormat("####")
val AmountDecimalFormat = DecimalFormat("#,###.##")

@Composable
fun AccountItem(account: Account, onAccountSelected: () -> Unit) {
    Row(
        modifier = Modifier.clickable {
           onAccountSelected()
        }.fillMaxWidth()
            .padding(RallyDefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemBar(account.color)
        Spacer(Modifier.width(RallyDefaultPadding))
        ItemInfo(
            title = account.name,
            digit = "• • • • •" + AccountDecimalFormat.format(account.number)
        )
        Spacer(Modifier.weight(1f))
        AmountItem(amount = account.balance, onAccountSelected)
    }
}

@Composable
fun BillItem(bill: Bill, onBillSelected: () -> Unit) {
    Row(
        modifier = Modifier.clickable {
            onBillSelected()
        }.fillMaxWidth().padding(RallyDefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemBar(bill.color)
        Spacer(Modifier.width(RallyDefaultPadding))
        ItemInfo(
            title = bill.name,
            digit = "Due " + bill.due
        )
        Spacer(Modifier.weight(1f))
        AmountItem(amount = bill.amount, onBillSelected)
    }
}



@Composable
fun ItemBar(color: Color) {
    Spacer(
        modifier = Modifier
            .size(4.dp, 36.dp)
            .background(color = color)
    )
}

@Composable
fun ItemInfo(title: String, digit: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.body1)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = digit, style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun AmountItem(amount: Float, onClick: () -> Unit) {
    val dollarSign = if (amount < 0) "- $" else "$"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = dollarSign + " " + AmountDecimalFormat.format(amount),
            style = MaterialTheme.typography.body2
        )
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "")
        }
    }
}


@Composable
fun <T> ItemsTotal(items: List<T>, getColor: (T) -> Color, getValue: (T) -> Float) {
    val colors = items.map {
        getColor(it)
    }

    val total = items.map {
        getValue(it)
    }.sum()

    val proportions = items.map{
        getValue(it)/total
    }

    Box(Modifier.padding(RallyDefaultPadding)) {
        AnimatedCircle(
            proportions = proportions, colors = colors,
            Modifier
                .height(300.dp)
                .align(
                    Alignment.Center
                )
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total", style = MaterialTheme.typography.body1)
            Text(text = AmountDecimalFormat.format(total), style = MaterialTheme.typography.h2)
        }
    }
}