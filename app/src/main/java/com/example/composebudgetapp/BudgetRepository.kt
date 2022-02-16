package com.example.composebudgetapp

import com.example.composebudgetapp.data.FakeData
import com.example.composebudgetapp.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface BudgetRepository {
    suspend fun getAccounts(): UserData
}

class FakeDataBudgetRepository: BudgetRepository {
    override suspend fun getAccounts(): UserData = withContext(Dispatchers.IO){
        delay(1000)
        return@withContext FakeData
    }
}