package com.example.subskepsv2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.subskepsv2.ui.screens.HomeScreen
import com.example.subskepsv2.ui.screens.AddSubscriptionScreen
import com.example.subskepsv2.ui.screens.SettingsScreen
import com.example.subskepsv2.viewmodel.SubscriptionViewModel
import com.example.subskepsv2.viewmodel.ThemeViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    subscriptionViewModel: SubscriptionViewModel,
    themeViewModel: ThemeViewModel
) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            val subscriptions by subscriptionViewModel.subscriptions.collectAsState()

            HomeScreen(
                subscriptions = subscriptions,
                onNavigateToAdd = { navController.navigate("add_subscription") },
                onNavigateToSettings = { navController.navigate("settings") },
                onDeleteSubscription = { subscriptionViewModel.deleteSubscription(it) }
            )
        }

        composable("add_subscription") {
            AddSubscriptionScreen(
                onSave = { name, price, cycle, billingDate ->
                    subscriptionViewModel.addSubscription(name, price, cycle, billingDate)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                themeViewModel = themeViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}