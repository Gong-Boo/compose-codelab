package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewScreen
import com.example.compose.rally.ui.theme.RallyTheme

class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp(RallyDestination.Overview, RallyDestination.Accounts, RallyDestination.Bills)
        }
    }
}

@Composable
fun RallyApp(vararg tab: RallyDestination) {
    RallyTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = tab
            .find { it.route == currentDestination?.route }
            ?: RallyDestination.Overview

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = tab.toList(),
                    onTabSelected = { screen ->
                        navController.navigateSingleTopTo(screen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navHostController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = RallyDestination.Overview.route,
        modifier = modifier
    ) {
        composable(route = RallyDestination.Overview.route) {
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navHostController.navigateSingleTopTo(RallyDestination.Accounts.route)
                },
                onClickSeeAllBills = {
                    navHostController.navigateSingleTopTo(RallyDestination.Bills.route)
                },
                onAccountClick = navHostController::navigateToSingleAccount
            )
        }
        composable(route = RallyDestination.Accounts.route) {
            AccountsScreen(
                onAccountClick = navHostController::navigateToSingleAccount
            )
        }
        composable(route = RallyDestination.Bills.route) {
            BillsScreen()
        }
        composable(
            route = RallyDestination.SingleAccount.routeWithArgs,
            arguments = RallyDestination.SingleAccount.arguments,
            deepLinks = RallyDestination.SingleAccount.deepLinks
        ) { backStackEntry ->
            val accountType = backStackEntry.arguments?.getString(RallyDestination.SingleAccount.accountTypeArg)
            SingleAccountScreen(accountType)
        }
    }
}

private fun NavHostController.navigateSingleTopTo(route: String) =
    navigate(route = route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    navigateSingleTopTo("${RallyDestination.SingleAccount.route}/$accountType")
}