package com.example.composebudgetapp.fragments

import androidx.lifecycle.ViewModel
import com.example.composebudgetapp.AppState
import com.example.composebudgetapp.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(repository: BudgetRepository): ViewModel() {
    val appState: StateFlow<AppState> = repository.appState
}