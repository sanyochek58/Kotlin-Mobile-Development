package com.example.task5_1.repository

import android.content.Context
import com.example.task5_1.data.DiaryEntry
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryRepository (private val context: Context){

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun loadEntries(): List<DiaryEntry> {
        return context.filesDir
            .listFiles() { file -> file.name.endsWith(".txt") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { file ->  file.toEntry() }
            ?: emptyList()
    }

    fun saveEntry(title: String, text: String): DiaryEntry {
        val timestamp = System.currentTimeMillis()
        val safeName = title
            .trim()
            .replace(Regex("[^A-Za-zА-Яа-яЁё0-9_\\- ]"), "")
            .take(40)
            .replace(" ", "_")

        val fileName = if (safeName.isNotEmpty()) "${timestamp}_${safeName}.txt" else "${timestamp}.txt"
        val file = File(context.filesDir, fileName)
        file.writeText(buildFileContent(title, text))

        return DiaryEntry(
            fileName = fileName,
            title = title.ifBlank { formatDate(timestamp) },
            preview = text.take(40).replace("\n", " "),
            dateMillis = timestamp
        )
    }

    fun readEntry(fileName: String): Pair<String, String> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return Pair("", "")
        val content = file.readText()
        // Формат: первая строка — TITLE:<заголовок>, далее — текст
        val lines = content.lines()
        val title = if (lines.isNotEmpty() && lines[0].startsWith("TITLE:")) {
            lines[0].removePrefix("TITLE:")
        } else ""
        val text = lines.drop(1).joinToString("\n")
        return Pair(title, text)
    }

    fun updateEntry(fileName: String, title: String, text: String) {
        val file = File(context.filesDir, fileName)
        file.writeText(buildFileContent(title, text))
    }

    fun deleteEntry(fileName: String) {
        File(context.filesDir, fileName).delete()
    }

    private fun buildFileContent(title: String, text: String): String = "TITLE:$title\n$text"

    private fun File.toEntry(): DiaryEntry {
        val content = runCatching { readText() }.getOrDefault("")
        val lines = content.lines()
        val title = if (lines.isNotEmpty() && lines[0].startsWith("TITLE:")) {
            lines[0].removePrefix("TITLE:").takeIf { it.isNotBlank() }
                ?: formatDate(lastModified())
        } else formatDate(lastModified())
        val text = lines.drop(1).joinToString(" ")
        return DiaryEntry(
            fileName = name,
            title = title,
            preview = text.take(40).replace("\n", " "),
            dateMillis = lastModified()
        )
    }

    private fun formatDate(millis: Long) = dateFormat.format(Date(millis))
}