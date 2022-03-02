package com.example.composebudgetapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.composebudgetapp.AppState
import com.example.composebudgetapp.MainViewModel
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.ui.AccountsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsFragment : Fragment() {
    private val viewModel by viewModels<AccountsViewModel>()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accounts = arguments?.get("extra")
        return ComposeView(requireContext()).apply {
            setContent {
                val appState by viewModel.appState.collectAsState()
                when (appState) {
                    is AppState.SUCCESS_LOADING -> {
                        val param =
                            if (accounts != null) accounts as List<Account> else (appState as AppState.SUCCESS_LOADING).userData.accountList
                        AccountsScreen(accounts = param) {
                            activityViewModel.navigateToAccounts(listOf(it))
                        }
                    }
                }
            }
        }
    }

}