package com.example.task5_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.task5_1.screen.DiaryListScreen
import com.example.task5_1.screen.EditEntryScreen
import com.example.task5_1.screen.NewEntryScreen
import com.example.task5_1.viewmodel.DiaryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryApp(viewModel)
        }
    }
}

sealed class Screen {
    object List : Screen()
    object NewEntry : Screen()
    data class EditEntry(val fileName: String) : Screen()
}

@Composable
fun DiaryApp(viewModel: DiaryViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }
    val openEntry by viewModel.openEntry.collectAsState()

    LaunchedEffect(openEntry) {
        if (openEntry != null) {
            currentScreen = Screen.EditEntry(openEntry!!.first)
        }
    }

    when (val screen = currentScreen) {
        is Screen.List -> {
            DiaryListScreen(
                viewModel = viewModel,
                onNewEntry = { currentScreen = Screen.NewEntry },
                onOpenEntry = { fileName ->
                    viewModel.openEntry(fileName)
                }
            )
        }

        is Screen.NewEntry -> {
            NewEntryScreen(
                onSave = { title, text -> viewModel.saveEntry(title, text) },
                onBack = { currentScreen = Screen.List }
            )
        }

        is Screen.EditEntry -> {
            val entry = openEntry
            if (entry != null && entry.first == screen.fileName) {
                EditEntryScreen(
                    fileName = entry.first,
                    initialTitle = entry.second,
                    initialText = entry.third,
                    onSave = { fileName, title, text ->
                        viewModel.updateEntry(fileName, title, text)
                    },
                    onBack = {
                        viewModel.clearOpenEntry()
                        currentScreen = Screen.List
                    }
                )
            }
        }
    }
}