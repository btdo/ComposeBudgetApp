package com.example.composebudgetapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.data.FakeData
import com.example.composebudgetapp.data.UserData
import com.example.composebudgetapp.ui.theme.ComposeBudgetAppTheme
import java.text.DecimalFormat

private val RallyDefaultPadding = 12.dp

@Preview
@Composable
fun OverviewPreview() {
    ComposeBudgetAppTheme {
        OverviewScreen(userData = FakeData)
    }
}

@Composable
fun OverviewScreen(userData: UserData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AlertCard()
        AccountsCard(userData.accountList, {}, {})
        BillsCard(userData.billList, {}, {})
    }
}

@Composable
fun AlertCard() {
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month."
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = RallyDefaultPadding)
    ) {
        Column {
            AlertHeader()
            AppDivider(modifier = Modifier.padding(horizontal = RallyDefaultPadding))
            AlertItem(alertMessage, {})
        }
    }
}

@Preview
@Composable
fun AlertHeaderPreview() {
    AlertHeader()
}

@Composable
fun AlertHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = RallyDefaultPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Alerts", style = MaterialTheme.typography.subtitle2)
        TextButton(onClick = { }) {
            Text(text = "See All", style = MaterialTheme.typography.button)
        }
    }
}


@Preview
@Composable
fun AlertContentPreview() {
    val alertMessage = "Heads up, you've used up 90% of your Shopping budget for this month."
    AlertItem(alertMessage, {})
}

@Composable
fun AlertItem(message: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = RallyDefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Sort, contentDescription = "Alert")
        }
    }

}

@Composable
fun AppDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colors.background, thickness = 1.dp, modifier = modifier)
}

@Composable
fun AccountsCard(accounts: List<Account>, onAccountSelected: (Account) -> Unit, onSelectAll: () -> Unit) {
    var total = accounts.map { account -> account.balance }.sum()
    val totalStr = AmountDecimalFormat.format(total)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = RallyDefaultPadding,
                start = RallyDefaultPadding,
                end = RallyDefaultPadding
            )
    ) {
        Column {
            ItemsOverview(title = "Account", total = totalStr)
            AccountHeaderDivider(accounts = accounts)
            accounts.take(SHOWN_ITEMS).forEach {
                AccountItem(account = it, modifier = Modifier.clickable {
                    onAccountSelected(it)
                })
                AppDivider()
            }
            SeeAllButton(onSelectAll = onSelectAll)
        }
    }
}


@Composable
fun BillsCard(bills: List<Bill>, onBillSelected: (Bill) -> Unit, onSelectAll: () -> Unit) {
    var total = bills.map { bill -> bill.amount }.sum()
    val totalStr = AmountDecimalFormat.format(total)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = RallyDefaultPadding,
                start = RallyDefaultPadding,
                end = RallyDefaultPadding
            )
    ) {
        Column {
            ItemsOverview(title = "Bill", total = totalStr)
            BillsHeaderDivider(bills = bills)
            bills.take(SHOWN_ITEMS).forEach {
                BillItem(bill = it, modifier = Modifier.clickable {
                    onBillSelected(it)
                })
                AppDivider()
            }
            SeeAllButton(onSelectAll = onSelectAll)
        }
    }
}

@Composable
fun SeeAllButton(onSelectAll: () -> Unit){
    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        TextButton(onClick = onSelectAll) {
            Text(text = "SEE ALL")
        }
    }
}

@Composable
fun ItemsOverview(title: String, total: String) {
    Column(modifier = Modifier.padding(RallyDefaultPadding)) {
        Text(text = title, style = MaterialTheme.typography.subtitle2)
        Text(text = "$$total", style = MaterialTheme.typography.h2)
    }
}

@Composable
fun AccountHeaderDivider(accounts: List<Account>) {
    Row(Modifier.fillMaxWidth()) {
        accounts.forEach {
            Divider(modifier = Modifier.weight(it.balance), color = it.color, thickness = 1.dp)
        }
    }
}

@Composable
fun BillsHeaderDivider(bills: List<Bill>) {
    Row(Modifier.fillMaxWidth()) {
        bills.forEach {
            Divider(modifier = Modifier.weight(it.amount), color = it.color, thickness = 1.dp)
        }
    }
}


@Composable
fun AccountItem(account: Account, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
        AmountItem(amount = account.balance, {})
    }
}


@Composable
fun BillItem(bill: Bill, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(RallyDefaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemBar(bill.color)
        Spacer(Modifier.width(RallyDefaultPadding))
        ItemInfo(
            title = bill.name,
            digit = "Due " + bill.due
        )
        Spacer(Modifier.weight(1f))
        AmountItem(amount = bill.amount, {})
    }
}

@Composable
fun ItemBar(color: Color) {
    Spacer(modifier = Modifier
        .size(4.dp, 36.dp)
        .background(color = color))
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



private val AccountDecimalFormat = DecimalFormat("####")
private val AmountDecimalFormat = DecimalFormat("#,###.##")
private const val SHOWN_ITEMS = 3