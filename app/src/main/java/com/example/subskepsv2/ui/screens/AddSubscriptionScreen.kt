package com.example.subskepsv2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddSubscriptionScreen(
    onSave: (String, String) -> Unit, // Callback untuk mengirim data balik
    onBack: () -> Unit
) {
    // State lokal untuk menampung ketikan user sebelum disimpan
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(Modifier
            .padding(padding)
            .padding(16.dp)) {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Nama Layanan") })
            TextField(value = price, onValueChange = { price = it }, label = { Text("Harga") })

            Button(onClick = {
                if(name.isNotEmpty()) onSave(name, price) // Panggil onSave
            }) {
                Text("Simpan")
            }
        }
    }
}