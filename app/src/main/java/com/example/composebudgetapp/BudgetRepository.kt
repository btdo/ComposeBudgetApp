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
    val userData: StateFlow<UserData>
    val currentScreen: StateFlow<AppScreen>
    suspend fun initialize()
    fun navigateToOverview()
    fun navigateToAccounts(accounts: List<Account>? = null)
    fun navigateToBills(bills: List<Bill>? = null)
    fun updateScreen(appScreen: AppScreen)
}

class FakeDataBudgetRepository : BudgetRepository {
    private val _appState = MutableStateFlow<AppState>(AppState.LOADING)
    override val appState: StateFlow<AppState> = _appState
    private var _userData =
        MutableStateFlow(UserData(accountList = emptyList(), billList = emptyList()))
    override val userData: StateFlow<UserData> = _userData
    private val _currentScreen = MutableStateFlow(AppScreen.Overview)
    override val currentScreen: StateFlow<AppScreen> = _currentScreen

    private fun setState(state: AppState) {
        _appState.value = state
    }

    override suspend fun initialize() {
        val data = getUserData()
        _appState.value = AppState.SUCCESS_LOADING.OverviewNavigationState
        _userData.value = data
    }

    private suspend fun getUserData(): UserData = withContext(Dispatchers.IO) {
        delay(1000)
        return@withContext FakeData
    }

    override fun navigateToOverview() {
        setState(AppState.SUCCESS_LOADING.OverviewNavigationState)
    }

    override fun navigateToAccounts(accounts: List<Account>?) {
        setState(AppState.SUCCESS_LOADING.AccountsNavigationState(accounts))
    }

    override fun navigateToBills(bills: List<Bill>?) {
        setState(AppState.SUCCESS_LOADING.BillsNavigationState(bills))
    }

    override fun updateScreen(appScreen: AppScreen) {
        _currentScreen.value = appScreen
    }
}

sealed class AppState {
    object LOADING : AppState()
    object ERROR : AppState()
    sealed class SUCCESS_LOADING(val screen: AppScreen) : AppState() {
        object OverviewNavigationState : SUCCESS_LOADING(
            AppScreen.Overview
        )

        data class AccountsNavigationState(val accounts: List<Account>? = null) :
            SUCCESS_LOADING(AppScreen.Accounts)

        data class BillsNavigationState(val bills: List<Bill>? = null) :
            SUCCESS_LOADING(AppScreen.Bills)
    }
}