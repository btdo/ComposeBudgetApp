package com.example.composebudgetapp.fragments

import androidx.lifecycle.ViewModelProvider
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
import com.example.composebudgetapp.R
import com.example.composebudgetapp.collectAsStateLifeCycle
import com.example.composebudgetapp.data.Account
import com.example.composebudgetapp.data.Bill
import com.example.composebudgetapp.ui.AccountsScreen
import com.example.composebudgetapp.ui.BillsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillsFragment : Fragment() {

    private val viewModel by viewModels<BillsViewModel>()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accounts = arguments?.get("extra")
        return ComposeView(requireContext()).apply {
            setContent {
                val appState by viewModel.appState.collectAsStateLifeCycle()
                when (appState) {
                    is AppState.SUCCESS_LOADING -> {
                        val param =
                            if (accounts != null) accounts as List<Bill> else (appState as AppState.SUCCESS_LOADING).userData.billList
                        BillsScreen(bills = param) {
                            activityViewModel.navigateToBills(listOf(it))
                        }
                    }
                }
            }
        }
    }
}