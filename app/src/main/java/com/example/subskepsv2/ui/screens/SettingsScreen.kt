package com.example.subskepsv2.ui.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.subskepsv2.viewmodel.ThemeViewModel
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val notificationsEnabled by themeViewModel.notificationsEnabled.collectAsState()
    val reminderDays by themeViewModel.reminderDaysBefore.collectAsState()
    val context = LocalContext.current

    val themeOptions = listOf("Sistem", "Terang", "Gelap")
    val themeValues = listOf(null, false, true)
    val reminderOptions = listOf(1, 3, 7)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section: Tema
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tema Aplikasi", style = MaterialTheme.typography.titleMedium)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    themeOptions.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = themeOptions.size),
                            onClick = { themeViewModel.setTheme(themeValues[index]) },
                            selected = isDarkTheme == themeValues[index]
                        ) {
                            Text(label, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Divider()

            // Section: Notifikasi
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pengingat Tagihan", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Dapatkan notifikasi sebelum jatuh tempo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { themeViewModel.setNotificationsEnabled(it) }
                    )
                }

                if (notificationsEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ingatkan saya sebelum:", style = MaterialTheme.typography.labelLarge)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        reminderOptions.forEachIndexed { index, days ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = reminderOptions.size),
                                onClick = { themeViewModel.setReminderDays(days) },
                                selected = reminderDays == days
                            ) {
                                Text("H-$days", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Divider()

            // Section: Simulasi & Demo
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Simulasi & Demo", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { simulatePaymentNotification(context) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Simulasi Pembayaran (Netflix)")
                }
                Text(
                    "Pastikan 'Akses Notifikasi' sudah aktif untuk Subskeps di pengaturan HP agar simulasi ini terbaca otomatis ke Dashboard.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

private fun simulatePaymentNotification(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "payment_simulation"
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Simulasi Pembayaran", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("E-Wallet")
        .setContentText("Pembayaran Rp 186.000 ke Netflix berhasil")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notificationManager.notify(Random().nextInt(), builder.build())
}
