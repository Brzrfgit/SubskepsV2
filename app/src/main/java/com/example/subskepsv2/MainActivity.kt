package com.example.subskepsv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Tambahkan ini
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.subskepsv2.ui.navigation.AppNavigation
import com.example.subskepsv2.ui.theme.SubskepsV2Theme
import com.example.subskepsv2.viewmodel.SubscriptionViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.subskepsv2.data.AppDatabase
import com.example.subskepsv2.data.SubscriptionRepository

class MainActivity : ComponentActivity() {
    // Inisialisasi ViewModel secara global di Activity
    private val viewModel: SubscriptionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(applicationContext)
                val repository = SubscriptionRepository(database.subscriptionDao())
                return SubscriptionViewModel(repository) as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SubskepsV2Theme {
                val navController = rememberNavController()

                // Masukkan viewModel ke dalam Navigasi
                AppNavigation(navController = navController, viewModel = viewModel)
            }
        }
    }


}