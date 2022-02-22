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
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.composebudgetapp.databinding.MainActivityBinding
import com.example.composebudgetapp.ui.*
import com.example.composebudgetapp.ui.theme.ComposeBudgetAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            viewModel.updateCurrentScreen(AppScreen.valueOf(destination.id!!))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                BudgetApp(viewModel, this::findNavController, this::addOnDestinationChangedListener)
            }
        }
    }

    private fun addOnDestinationChangedListener(){
        findNavController().addOnDestinationChangedListener(listener)
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
    getNavController: () -> NavController,
    onViewCreated: () -> Unit
) {
    ComposeBudgetAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            val appState by viewModel.appState.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            when(appState) {
                is AppState.SUCCESS_LOADING -> {
                    navigateToScreen(appState = appState, getNavController = getNavController)
                }
            }

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

private fun navigateToScreen(
    appState: AppState,
    getNavController: () -> NavController
) {
    var bundle: Bundle? = null
    when (appState) {
        is AppState.SUCCESS_LOADING.OverviewNavigationState -> {
            getNavController().popBackStack(
                (appState as AppState.SUCCESS_LOADING).screen.navigation,
                false
            )
        }
        is AppState.SUCCESS_LOADING.AccountsNavigationState -> {
            val accounts =
                appState.accounts
            bundle = bundleOf("extra" to accounts)
            getNavController().navigate(
                (appState as AppState.SUCCESS_LOADING).screen.navigation,
                bundle
            )
        }
        is AppState.SUCCESS_LOADING.BillsNavigationState -> {
            val bills =
                appState.bills
            bundle = bundleOf("extra" to bills)
            getNavController().navigate(
                (appState as AppState.SUCCESS_LOADING).screen.navigation,
                bundle
            )
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
