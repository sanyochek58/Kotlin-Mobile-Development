package com.example.task4_12.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task4_12.viewmodel.AnimalFactsViewModel
import com.example.task4_12.viewmodel.FactUiState


@Composable
fun AnimalFactsScreen(viewModel: AnimalFactsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Область контента
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is FactUiState.Idle -> {
                    Text("Нажми кнопку, чтобы узнать факт!")
                }
                is FactUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is FactUiState.Success -> {
                    Card(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = state.fact,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.loadFact() },
            enabled = uiState !is FactUiState.Loading
        ) {
            Text("Новый факт!")
        }
    }
}