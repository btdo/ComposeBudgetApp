package com.example.composebudgetapp.di.module

import com.example.composebudgetapp.BudgetRepository
import com.example.composebudgetapp.FakeDataBudgetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideBudgetRepository(): BudgetRepository = FakeDataBudgetRepository()
}