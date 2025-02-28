package com.sonkajarvi.calculator

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sonkajarvi.calculator.ui.CalculatorScreen
import com.sonkajarvi.calculator.ui.HistoryScreen
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

enum class Screen(@StringRes val title: Int) {
    Calculator(title = R.string.app_name),
    History(title = R.string.history_title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorAppBar(
    current: Screen,
    canNavigate: Boolean,
    onNavigate: () -> Unit,
    actions: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(stringResource(current.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(R.color.gray_lighter),
            titleContentColor = colorResource(R.color.gray_dark)
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigate) {
                IconButton(onClick = onNavigate) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (actions != null) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.dropdown_menu)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        actions()
                    }
                }
            }
        }
    )
}

@Composable
fun CalculatorApp(
    navController: NavHostController = rememberNavController(),
    viewModel: EntryViewModel
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Calculator.name
    )
    var actions by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    Scaffold(
        topBar = {
            CalculatorAppBar(
                current = currentScreen,
                canNavigate = navController.previousBackStackEntry != null,
                onNavigate = { navController.navigateUp() },
                actions = actions
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screen.Calculator.name) {
                actions = {
                    DropdownMenuItem(
                        text = { Text("History") },
                        onClick = {
                            navController.navigate(Screen.History.name)
                        }
                    )
                }

                CalculatorScreen(viewModel)
            }
            composable(route = Screen.History.name) {
                actions = null

                HistoryScreen(viewModel)
            }
        }
    }
}
