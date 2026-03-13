package com.example.task4_9.workers

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.task4_9.R

class ReportWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("weather", Context.MODE_PRIVATE)

        val moscow = prefs.getString("Москва", "нет данных")
        val london = prefs.getString("Лондон", "нет данных")
        val newYork = prefs.getString("Нью-Йорк", "нет данных")

        val temps = listOf(moscow, london, newYork).mapNotNull { it?.substringBefore(" ")?.toIntOrNull() }
        val avg = if (temps.isNotEmpty()) temps.average().toInt() else 0

        showNotification("Отчёт готов! Средняя температура: $avg C")
        return Result.success(workDataOf("avg_temp" to avg))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(message: String){
        val notification = Notification.Builder(applicationContext, "weather_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Погода собрана!")
            .setContentText(message)
            .build()

        applicationContext.getSystemService(NotificationManager::class.java)
            .notify(1, notification)
    }

}