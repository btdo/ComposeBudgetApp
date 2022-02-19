package com.example.composebudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebudgetapp.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BudgetRepository): ViewModel() {
    val appState: StateFlow<AppState> = repository.appState

    fun getUserData(){
        viewModelScope.launch {
            repository.getAccounts()
        }
    }
}

