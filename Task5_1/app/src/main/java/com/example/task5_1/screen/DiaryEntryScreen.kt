package com.example.task5_1.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    onSave: (title: String, text: String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    EntryEditScaffold(
        screenTitle = "Новая запись",
        title = title,
        text = text,
        onTitleChange = { title = it },
        onTextChange = { text = it },
        onSave = {
            if (text.isNotBlank()) {
                onSave(title, text)
                onBack()
            }
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    fileName: String,
    initialTitle: String,
    initialText: String,
    onSave: (fileName: String, title: String, text: String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var text by remember { mutableStateOf(initialText) }

    EntryEditScaffold(
        screenTitle = "Редактирование",
        title = title,
        text = text,
        onTitleChange = { title = it },
        onTextChange = { text = it },
        onSave = {
            if (text.isNotBlank()) {
                onSave(fileName, title, text)
                onBack()
            }
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryEditScaffold(
    screenTitle: String,
    title: String,
    text: String,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Заголовок (опционально)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text("Текст записи") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 6
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }
                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    enabled = text.isNotBlank()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}