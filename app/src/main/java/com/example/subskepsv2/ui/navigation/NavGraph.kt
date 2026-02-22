package com.example.subskepsv2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.subskepsv2.ui.screens.HomeScreen
import com.example.subskepsv2.ui.screens.AddSubscriptionScreen
import com.example.subskepsv2.viewmodel.SubscriptionViewModel

@Composable
fun AppNavigation(
navController: NavHostController,
viewModel: SubscriptionViewModel // 1. Terima ViewModel di sini
) {
    NavHost(navController = navController, startDestination = "home") {

        // HALAMAN HOME
        composable("home") {
            // 2. Observasi State: Ambil data terbaru dari ViewModel
            val subscriptions by viewModel.subscriptions.collectAsState()

            HomeScreen(
                subscriptions = subscriptions, // Kirim list data ke UI
                onNavigateToAdd = { navController.navigate("add_subscription") }
            )
        }

        // HALAMAN TAMBAH
        composable("add_subscription") {
            AddSubscriptionScreen(
                onSave = { name, price ->
                    // 3. Kirim Aksi: Beritahu ViewModel untuk menambah data
                    viewModel.addSubscription(name, price)
                    navController.popBackStack() // Kembali ke Home setelah simpan
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}