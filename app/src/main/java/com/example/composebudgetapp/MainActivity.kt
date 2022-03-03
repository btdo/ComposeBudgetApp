package com.example.composebudgetapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.composebudgetapp.databinding.MainActivityBinding
import com.example.composebudgetapp.ui.*
import com.example.composebudgetapp.ui.theme.ComposeBudgetAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            viewModel.updateScreen(AppScreen.valueOf(destination.id!!))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                BudgetApp(viewModel) {
                    findNavController().addOnDestinationChangedListener(listener = listener)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigate.collect {
                    navigateToScreen(it)
                }
            }
        }
    }

    private fun navigateToScreen(
        navigationState: NavigationState
    ) {
        var bundle: Bundle? = null
        when (navigationState) {
            is NavigationState.OverviewNavigationState -> {
                findNavController().popBackStack(
                    navigationState.screen.navigation,
                    false
                )
            }
            is NavigationState.AccountsNavigationState -> {
                val accounts = navigationState.accounts
                bundle = bundleOf("extra" to accounts)
                findNavController().navigate(
                    navigationState.screen.navigation,
                    bundle
                )
            }
            is NavigationState.BillsNavigationState -> {
                val bills = navigationState.bills
                bundle = bundleOf("extra" to bills)
                findNavController().navigate(
                    navigationState.screen.navigation,
                    bundle
                )
            }
        }
    }

    /**
     * See https://issuetracker.google.com/142847973
     */
    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }
}

@Composable
fun BudgetApp(
    viewModel: MainViewModel,
    onViewCreated: () -> Unit
) {
    ComposeBudgetAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            val appState by viewModel.appState.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState(AppScreen.Overview)
            when (appState) {
                is AppState.LOADING -> LoadingScreen()
                is AppState.ERROR -> Text(text = "Something went wrong")
                is AppState.SUCCESS_LOADING -> {
                    MainApp(
                        currentScreen = currentScreen, onViewCreated
                    ) {
                        when (it) {
                            AppScreen.Overview -> viewModel.navigateToOverview()
                            AppScreen.Accounts -> viewModel.navigateToAccounts()
                            AppScreen.Bills -> viewModel.navigateToBills()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MainApp(
    currentScreen: AppScreen = AppScreen.Overview,
    onViewCreated: () -> Unit,
    onBottomNavClicked: (AppScreen) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, bottomBar = {
        AppBottomNavigation(
            screens = AppScreen.values().toList(),
            currentSelected = currentScreen,
            onClicked = {
                onBottomNavClicked(it)
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            AndroidViewBinding(MainActivityBinding::inflate, update = {
                onViewCreated()
            })
        }
    }
}
