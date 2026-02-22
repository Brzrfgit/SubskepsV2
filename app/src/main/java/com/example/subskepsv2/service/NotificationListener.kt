package com.example.subskepsv2.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.subskepsv2.data.AppDatabase
import com.example.subskepsv2.data.SubscriptionRepository
import com.example.subskepsv2.data.model.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        Log.d("SubskepsNotification", "Package: $packageName, Title: $title, Text: $text")

        // Regex untuk mendeteksi pembayaran (Contoh: "Pembayaran Rp 100.000 ke Netflix berhasil")
        // Sesuaikan dengan format notifikasi e-wallet/bank di Indonesia
        val priceRegex = "Rp\\s?([\\d.]+)"
        val serviceKeywords = listOf("Netflix", "Spotify", "YouTube", "Disney", "Gopay", "OVO", "Dana")

        val pricePattern = Pattern.compile(priceRegex)
        val priceMatcher = pricePattern.matcher(text)

        if (priceMatcher.find()) {
            val amount = priceMatcher.group(1)?.replace(".", "") ?: ""
            val detectedService = serviceKeywords.find { 
                text.contains(it, ignoreCase = true) || title.contains(it, ignoreCase = true) 
            } ?: "Layanan Tak Dikenal"

            Log.d("SubskepsNotification", "Detected Service: $detectedService, Amount: $amount")

            // Simpan otomatis ke Database
            saveToDatabase(detectedService, amount)
        }
    }

    private fun saveToDatabase(name: String, price: String) {
        val database = AppDatabase.getDatabase(this)
        val repository = SubscriptionRepository(database.subscriptionDao())
        
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        val nextBilling = sdf.format(calendar.time)

        scope.launch {
            repository.insert(
                Subscription(
                    name = name,
                    price = price,
                    cycle = "Bulanan",
                    billingDate = nextBilling
                )
            )
        }
    }
}