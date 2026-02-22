package com.example.subskepsv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subskepsv2.data.SubscriptionRepository
import com.example.subskepsv2.data.model.Subscription
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    // Mengambil data dari database secara otomatis
    val subscriptions = repository.allSubscriptions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addSubscription(name: String, price: String, cycle: String, billingDate: String) {
        viewModelScope.launch {
            repository.insert(
                Subscription(
                    name = name,
                    price = price,
                    cycle = cycle,
                    billingDate = billingDate
                )
            )
        }
    }

    fun deleteSubscription(subscription: Subscription) {
        viewModelScope.launch {
            repository.delete(subscription)
        }
    }
}