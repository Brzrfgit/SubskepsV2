package com.example.subskepsv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subskepsv2.data.SettingsDao
import com.example.subskepsv2.data.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeViewModel(private val settingsDao: SettingsDao) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _reminderDaysBefore = MutableStateFlow(1)
    val reminderDaysBefore = _reminderDaysBefore.asStateFlow()

    private var currentSettings: AppSettings? = null

    init {
        viewModelScope.launch {
            settingsDao.getSettings().collectLatest { settings ->
                currentSettings = settings
                _isDarkTheme.value = settings?.isDarkTheme
                _notificationsEnabled.value = settings?.notificationsEnabled ?: true
                _reminderDaysBefore.value = settings?.reminderDaysBefore ?: 1
            }
        }
    }

    fun setTheme(isDark: Boolean?) {
        updateSettings(isDarkTheme = isDark)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        updateSettings(notificationsEnabled = enabled)
    }

    fun setReminderDays(days: Int) {
        updateSettings(reminderDaysBefore = days)
    }

    private fun updateSettings(
        isDarkTheme: Boolean? = _isDarkTheme.value,
        notificationsEnabled: Boolean = _notificationsEnabled.value,
        reminderDaysBefore: Int = _reminderDaysBefore.value
    ) {
        viewModelScope.launch {
            settingsDao.saveSettings(
                AppSettings(
                    isDarkTheme = isDarkTheme,
                    notificationsEnabled = notificationsEnabled,
                    reminderDaysBefore = reminderDaysBefore
                )
            )
        }
    }
}