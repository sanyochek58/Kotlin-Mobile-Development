package com.example.task4_13.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(5000L)
                updateRate()
            }
        }
    }

    fun updateRate() {
        _uiState.update { current ->
            val newRate = 90.0 + (-2.0..2.0).random()
            current.copy(
                previousRate = current.rate,
                rate = newRate
            )
        }
    }
    private fun ClosedRange<Double>.random(): Double =
        start + Math.random() * (endInclusive - start)
}