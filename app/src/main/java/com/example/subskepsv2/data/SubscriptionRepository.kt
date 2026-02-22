package com.example.subskepsv2.data

import com.example.subskepsv2.data.model.Subscription
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(private val subscriptionDao: SubscriptionDao) {
    val allSubscriptions: Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()

    suspend fun insert(subscription: Subscription) {
        subscriptionDao.insertSubscription(subscription)
    }

    suspend fun delete(subscription: Subscription) {
        subscriptionDao.deleteSubscription(subscription)
    }
}