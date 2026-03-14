// screen/CurrencyScreen.kt
package com.example.task4_13.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task4_13.viewmodel.CurrencyViewModel

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "USD / RUB",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        val arrow = if (uiState.isUp) "▲" else "▼"
        val color = if (uiState.isUp) Color(0xFF2E7D32) else Color(0xFFC62828)

        Text(
            text = "$arrow ${"%.2f".format(uiState.rate)} ₽",
            fontSize = 48.sp,
            color = color
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Предыдущий: ${"%.2f".format(uiState.previousRate)} ₽",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { viewModel.updateRate() }) {
            Text("Обновить")
        }
    }
}