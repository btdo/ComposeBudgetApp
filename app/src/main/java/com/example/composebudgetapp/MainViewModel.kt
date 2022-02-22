package com.example.composebudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.ui.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BudgetRepository): ViewModel() {
    val appState: StateFlow<AppState> = repository.appState
    val currentScreen: StateFlow<AppScreen> = repository.currentScreen

    init{
        viewModelScope.launch {
            repository.initialize()
        }
    }

    fun updateCurrentScreen(screen: AppScreen){
        repository.updateScreen(screen)
    }

    fun navigateToOverview(){
        repository.navigateToOverview()
    }

    fun navigateToAccounts(accounts: List<Account>? = null){
        repository.navigateToAccounts(accounts)
    }

    fun navigateToBills(bills: List<Bill>? = null){
        repository.navigateToBills(bills)
    }
}

