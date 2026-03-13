package com.example.task4_9.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import kotlinx.coroutines.delay

class CityWeatherWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val city = inputData.getString("city") ?: return Result.failure()

        delay(2000L)

        val temperature = (-5..15).random()
        val weather = listOf("Солнечно","Дождь","Облачно","Снег").random()

        applicationContext.getSharedPreferences("weather", Context.MODE_PRIVATE)
            .edit()
            .putString(city as String?, "$temperature : $weather")
            .apply()

        return Result.success()
    }
}