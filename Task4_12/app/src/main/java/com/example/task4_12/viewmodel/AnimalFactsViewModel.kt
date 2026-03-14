package com.example.task4_12.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task4_12.data.AnimalFactsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimalFactsViewModel(private val repository: AnimalFactsRepository = AnimalFactsRepository()) : ViewModel(){
    private val _uiState = MutableStateFlow<FactUiState>(FactUiState.Idle)
    val uiState: StateFlow<FactUiState> = _uiState.asStateFlow()
    private var collectJob: Job? = null

    fun loadFact(){
        if(_uiState.value is FactUiState.Loading) return
        collectJob?.cancel()

        collectJob = viewModelScope.launch {
            _uiState.value = FactUiState.Loading

            repository.getRandomFact().catch {
                throwable -> _uiState.value = FactUiState.Success("Не удалось загрузить факт!")
            }.collect { fact ->
                _uiState.value = FactUiState.Success(fact)
            }
        }
    }
}
