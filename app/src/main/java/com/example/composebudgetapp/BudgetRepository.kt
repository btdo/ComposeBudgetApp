package com.example.composebudgetapp

import com.example.composebudgetapp.data.FakeData
import com.example.composebudgetapp.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

interface BudgetRepository {
    val appState: StateFlow<AppState>
    suspend fun initialize()
}

class FakeDataBudgetRepository : BudgetRepository {
    private val _appState = MutableStateFlow<AppState>(AppState.LOADING)
    override val appState: StateFlow<AppState> = _appState
    override suspend fun initialize() {
        val data = getUserData()
        _appState.value = AppState.SUCCESS_LOADING(data)
    }

    private suspend fun getUserData(): UserData = withContext(Dispatchers.IO) {
        delay(5000)
        return@withContext FakeData
    }
}

sealed class AppState {
    object LOADING : AppState()
    object ERROR : AppState()
    data class SUCCESS_LOADING(val userData: UserData) : AppState()
}