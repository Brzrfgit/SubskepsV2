package com.example.subskepsv2.data

import androidx.room.*
import com.example.subskepsv2.data.model.Subscription
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface SubscriptionDao {
    @androidx.room.Query("SELECT * FROM subscriptions")
    fun getAllSubscriptions(): Flow<List<Subscription>> // Mengambil semua data secara Real-time

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription)

    @androidx.room.Delete
    suspend fun deleteSubscription(subscription: Subscription)
}