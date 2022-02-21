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
import com.example.composebudgetapp.ui.OverviewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private val viewModel by viewModels<OverviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val appState by viewModel.appState.collectAsState()

                when (appState) {
                    is AppState.SUCCESS_LOADING.OverviewNavigationState -> OverviewScreen(userData = (appState as AppState.SUCCESS_LOADING.OverviewNavigationState).userOverview,
                        {
                            viewModel.navigateToAccounts()
                        }, {
                            viewModel.navigateToAccounts(listOf(it))
                        }, {
                            viewModel.navigateToBills()
                        }, {
                            viewModel.navigateToBills(listOf(it))
                        })
                }
            }
        }
    }
}