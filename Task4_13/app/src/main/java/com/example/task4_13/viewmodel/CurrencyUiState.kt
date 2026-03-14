package com.example.task4_13.viewmodel

data class CurrencyUiState(
    val rate: Double = 90.0,
    val previousRate: Double = 90.0
){
    val isUp: Boolean get() = rate >= previousRate
}
