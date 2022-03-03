package com.example.composebudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.ui.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BudgetRepository): ViewModel() {
    val appState: StateFlow<AppState> = repository.appState
    private val _navigate = MutableSharedFlow<NavigationState>(replay = 0)
    val navigate : SharedFlow<NavigationState> = _navigate
    private val _navScreen  = _navigate.map {
        it.screen
    }

    private val _currentScreen = MutableStateFlow(AppScreen.Overview)
    val currentScreen : Flow<AppScreen> = merge(_navScreen, _currentScreen)

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
        }
    }

    fun navigateToAccounts(accounts: List<Account>? = null){
        viewModelScope.launch {
            _navigate.emit(NavigationState.AccountsNavigationState(accounts = accounts))
        }
    }

    fun navigateToBills(bills: List<Bill>? = null){
        viewModelScope.launch {
            _navigate.emit(NavigationState.BillsNavigationState(bills = bills))
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

