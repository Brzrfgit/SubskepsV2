package com.example.subskepsv2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 0,
    val isDarkTheme: Boolean?, // null = system, false = light, true = dark
    val notificationsEnabled: Boolean = true,
    val reminderDaysBefore: Int = 1 // H-1, H-3, H-7, etc.
)