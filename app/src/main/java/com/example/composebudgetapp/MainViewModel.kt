package com.example.composebudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebudgetapp.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(val repository: BudgetRepository = FakeDataBudgetRepository()): ViewModel() {
    private val _appState = MutableStateFlow<AppState>(AppState.LOADING)
    val appState: StateFlow<AppState> = _appState

    fun getUserData(){
        viewModelScope.launch {
            _appState.value = AppState.SUCCESS_LOADING(repository.getAccounts())
        }
    }
}

sealed class AppState {
    object LOADING: AppState()
    object ERROR: AppState()
    data class SUCCESS_LOADING(val userData: UserData) : AppState()
}