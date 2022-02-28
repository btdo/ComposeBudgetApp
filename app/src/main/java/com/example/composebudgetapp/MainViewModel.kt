package com.example.composebudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.ui.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BudgetRepository): ViewModel() {
    val appState: StateFlow<AppState> = repository.appState
    private val _navigate = MutableSharedFlow<NavigationState>(replay = 0)
    val navigate : SharedFlow<NavigationState> = _navigate
    private val _currentScreen = MutableStateFlow(AppScreen.Overview)
    val currentScreen : StateFlow<AppScreen> = _currentScreen

    init{
        viewModelScope.launch {
            repository.initialize()
        }
    }

    fun updateScreen(appScreen: AppScreen){
        _currentScreen.value = appScreen
    }

    fun navigateToOverview(){
        viewModelScope.launch {
            _navigate.emit(NavigationState.OverviewNavigationState)
            _currentScreen.value = AppScreen.Overview
        }
    }

    fun navigateToAccounts(accounts: List<Account>? = null){
        viewModelScope.launch {
            _navigate.emit(NavigationState.AccountsNavigationState(accounts = accounts))
            _currentScreen.value = AppScreen.Accounts
        }
    }

    fun navigateToBills(bills: List<Bill>? = null){
        viewModelScope.launch {
            _navigate.emit(NavigationState.BillsNavigationState(bills = bills))
            _currentScreen.value = AppScreen.Bills
        }
    }
}


sealed class NavigationState(val screen: AppScreen) {
    object OverviewNavigationState : NavigationState(
        AppScreen.Overview
    )
    data class AccountsNavigationState(val accounts: List<Account>? = null) :
        NavigationState(AppScreen.Accounts)

    data class BillsNavigationState(val bills: List<Bill>? = null) :
        NavigationState(AppScreen.Bills)
}

