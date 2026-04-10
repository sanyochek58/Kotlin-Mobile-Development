package com.example.task5_1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.task5_1.data.DiaryEntry
import com.example.task5_1.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryViewModel(application: Application): AndroidViewModel(application) {

    private val repository = DiaryRepository(application.applicationContext)

    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries

    private val _openEntry = MutableStateFlow<Triple<String, String, String>?>(null)
    val openEntry: StateFlow<Triple<String, String, String>?> = _openEntry

    init {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) { repository.loadEntries() }
            _entries.value = loaded
        }
    }

    fun saveEntry(title: String, text: String) {
        viewModelScope.launch {
            val entry = withContext(Dispatchers.IO) { repository.saveEntry(title, text) }
            _entries.value = listOf(entry) + _entries.value
        }
    }

    fun deleteEntry(fileName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.deleteEntry(fileName) }
            _entries.value = _entries.value.filter { it.fileName != fileName }
        }
    }

    fun openEntry(fileName: String) {
        viewModelScope.launch {
            val (title, text) = withContext(Dispatchers.IO) { repository.readEntry(fileName) }
            _openEntry.value = Triple(fileName, title, text)
        }
    }

    fun updateEntry(fileName: String, title: String, text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.updateEntry(fileName, title, text) }
            // Обновляем preview в списке
            _entries.value = _entries.value.map { entry ->
                if (entry.fileName == fileName) {
                    entry.copy(
                        title = title.ifBlank { entry.title },
                        preview = text.take(40).replace("\n", " ")
                    )
                } else entry
            }
            _openEntry.value = null
        }
    }

    fun clearOpenEntry() {
        _openEntry.value = null
    }
}