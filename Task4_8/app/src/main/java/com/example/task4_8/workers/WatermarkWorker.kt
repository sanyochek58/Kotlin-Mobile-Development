package com.example.task4_8.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class WatermarkWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val file = inputData.getString("file") ?: return Result.failure()

        delay(3000L)

        val output = workDataOf("file" to "watermarked_$file")
        return Result.success(output)
    }

}