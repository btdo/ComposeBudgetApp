package com.example.composebudgetapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.composebudgetapp.R
import com.example.composebudgetapp.data.FakeData
import com.example.composebudgetapp.ui.OverviewScreen

class OverviewFragment : Fragment() {
    private val viewModel by viewModels<OverviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OverviewScreen(userData = FakeData)
            }
        }
    }
}