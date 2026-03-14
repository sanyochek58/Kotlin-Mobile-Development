package com.example.task4_12.viewmodel

sealed class FactUiState{
    object Idle: FactUiState()

    object Loading: FactUiState()

    data class Success(val fact: String): FactUiState()
}