package com.example.composebudgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.composebudgetapp.databinding.MainActivityBinding
import com.example.composebudgetapp.ui.AppBottomNavigation
import com.example.composebudgetapp.ui.AppScreen
import com.example.composebudgetapp.ui.LoadingScreen
import com.example.composebudgetapp.ui.theme.ComposeBudgetAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                BudgetApp(viewModel, this::findNavController)
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
fun BudgetApp(viewModel: MainViewModel, getNavController: () -> NavController) {
    ComposeBudgetAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            val appState by viewModel.appState.collectAsState()
            LaunchedEffect(true) {
                viewModel.getUserData()
            }

            when (appState) {
                is AppState.LOADING -> LoadingScreen()
                is AppState.ERROR -> Text(text = "Something went wrong")
                is AppState.SUCCESS_LOADING -> MainApp(getNavController)
            }
        }
    }
}

@Composable
fun MainApp(getNavController: () -> NavController ) {
    val scaffoldState = rememberScaffoldState()
    val currentScreen = remember{ mutableStateOf(AppScreen.Overview)}
    Scaffold(scaffoldState = scaffoldState, bottomBar = {
        AppBottomNavigation(
        screens = AppScreen.values().toList(),
        currentSelected = currentScreen.value,
        onClicked = {
            getNavController().navigate(it.navigation)
            currentScreen.value = it
        }
    )
    }) {
        Box(modifier = Modifier.padding(it)){
            AndroidViewBinding(MainActivityBinding::inflate)
        }
    }
}
