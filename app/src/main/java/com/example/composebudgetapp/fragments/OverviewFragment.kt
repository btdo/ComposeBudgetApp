package com.example.composebudgetapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.composebudgetapp.AppState
import com.example.composebudgetapp.MainViewModel
import com.example.composebudgetapp.R
import com.example.composebudgetapp.collectAsStateLifeCycle
import com.example.composebudgetapp.ui.OverviewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private val viewModel by viewModels<OverviewViewModel>()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val appState by viewModel.appState.collectAsStateLifeCycle()
                when(appState) {
                    is AppState.SUCCESS_LOADING -> {
                        OverviewScreen(userData = (appState as AppState.SUCCESS_LOADING).userData,
                            {
                                activityViewModel.navigateToAccounts()
                            }, {
                                activityViewModel.navigateToAccounts(listOf(it))
                            }, {
                                activityViewModel.navigateToBills()
                            }, {
                                activityViewModel.navigateToBills(listOf(it))
                            })
                    }
                }
            }
        }
    }
}