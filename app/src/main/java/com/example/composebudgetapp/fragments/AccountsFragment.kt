package com.example.composebudgetapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.composebudgetapp.AppState
import com.example.composebudgetapp.ui.AccountsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : Fragment() {
    private val viewModel by viewModels<AccountsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val appState by viewModel.appState.collectAsState()
                when(appState){
                    is AppState.SUCCESS_LOADING.AccountsNavigationState -> AccountsScreen(accounts = (appState as AppState.SUCCESS_LOADING.AccountsNavigationState).accounts, {})
                }
            }
        }
    }
}