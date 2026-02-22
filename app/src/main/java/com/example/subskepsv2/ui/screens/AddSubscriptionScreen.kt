package com.example.subskepsv2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    onSave: (String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    
    // Fungsi pembantu untuk menghitung tanggal berdasarkan siklus
    fun calculateDate(cycle: String): String {
        val calendar = Calendar.getInstance()
        when (cycle) {
            "Harian" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
            "Mingguan" -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
            "Bulanan" -> calendar.add(Calendar.MONTH, 1)
            "Tahunan" -> calendar.add(Calendar.YEAR, 1)
        }
        return sdf.format(calendar.time)
    }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var cycle by remember { mutableStateOf("Bulanan") }
    // Inisialisasi awal (default Bulanan -> 1 bulan ke depan)
    var billingDate by remember { mutableStateOf(calculateDate("Bulanan")) }
    
    var expanded by remember { mutableStateOf(false) }
    val cycles = listOf("Harian", "Mingguan", "Bulanan", "Tahunan")
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val date = datePickerState.selectedDateMillis?.let {
                        sdf.format(Date(it))
                    } ?: billingDate
                    billingDate = date
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tambah Langganan") })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Layanan (Netflix, Spotify, dll.)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Nominal Harga") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown untuk Siklus Tagihan
            Box {
                TextField(
                    value = cycle,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Siklus Tagihan") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, "contentDescription", 
                            Modifier.clickable { expanded = !expanded })
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    cycles.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                cycle = label
                                // Otomatis update tanggal saat siklus diubah
                                billingDate = calculateDate(label)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Date Picker untuk Tanggal Jatuh Tempo
            TextField(
                value = billingDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tanggal Jatuh Tempo") },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, "Select Date",
                        Modifier.clickable { showDatePicker = true })
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (name.isNotEmpty() && price.isNotEmpty() && billingDate.isNotEmpty()) {
                        onSave(name, price, cycle, billingDate)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}