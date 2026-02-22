package com.example.subskepsv2

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.subskepsv2.data.AppDatabase
import com.example.subskepsv2.data.SubscriptionRepository
import com.example.subskepsv2.ui.navigation.AppNavigation
import com.example.subskepsv2.ui.theme.SubskepsV2Theme
import com.example.subskepsv2.viewmodel.SubscriptionViewModel
import com.example.subskepsv2.viewmodel.ThemeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    private val subscriptionViewModel: SubscriptionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(applicationContext)
                val repository = SubscriptionRepository(database.subscriptionDao())
                return SubscriptionViewModel(repository) as T
            }
        }
    }

    private val themeViewModel: ThemeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getDatabase(applicationContext)
                return ThemeViewModel(database.settingsDao()) as T
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Minta izin notifikasi untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            SubskepsV2Theme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    subscriptionViewModel = subscriptionViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}