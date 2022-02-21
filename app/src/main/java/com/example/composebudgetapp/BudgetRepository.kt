package com.example.composebudgetapp

import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.data.FakeData
import com.example.composebudgetapp.data.UserData
import com.example.composebudgetapp.ui.AppScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

interface BudgetRepository {
    val appState: StateFlow<AppState>
    suspend fun initialize()
    fun navigateToOverview()
    fun navigateToAccounts(accounts: List<Account>? = null)
    fun navigateToBills(bills: List<Bill>? = null)
}

class FakeDataBudgetRepository: BudgetRepository {
    private val _appState = MutableStateFlow<AppState>(AppState.LOADING)
    override val appState: StateFlow<AppState> = _appState
    private var userData: UserData? = null

    private fun setState(state: AppState) {
        _appState.value = state
    }

    override suspend fun initialize() {
        val data  = getUserData()
        _appState.value = AppState.SUCCESS_LOADING.OverviewNavigationState(data)
        userData = data
    }

    private suspend fun getUserData(): UserData = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext FakeData
    }

    override fun navigateToOverview() {
        userData?.let {
            setState(AppState.SUCCESS_LOADING.OverviewNavigationState(it))
        }
    }

    override fun navigateToAccounts(accounts: List<Account>?) {
        if (accounts == null){
            userData?.let {
                setState(AppState.SUCCESS_LOADING.AccountsNavigationState(it.accountList))
            }
            return
        }
        setState(AppState.SUCCESS_LOADING.AccountsNavigationState(accounts))
    }

    override fun navigateToBills(bills: List<Bill>?) {
        if (bills == null) {
            userData?.let {
                setState(AppState.SUCCESS_LOADING.BillsNavigationState(it.billList))
            }
            return
        }
        setState(AppState.SUCCESS_LOADING.BillsNavigationState(bills))
    }
}

sealed class AppState {
    object LOADING : AppState()
    object ERROR : AppState()
    sealed class SUCCESS_LOADING(val screen: AppScreen) : AppState() {
        data class OverviewNavigationState(val userOverview: UserData) : SUCCESS_LOADING(
            AppScreen.Overview
        )

        data class AccountsNavigationState(val accounts: List<Account>) :
            SUCCESS_LOADING(AppScreen.Accounts)

        data class BillsNavigationState(val bills: List<Bill>) : SUCCESS_LOADING(AppScreen.Bills)
    }
}