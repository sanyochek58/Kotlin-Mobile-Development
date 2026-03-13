package com.example.task4_8.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class CompressWorker (context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        delay(3000L)

        val output = workDataOf("file" to "compressed_photo.jpg")
        return Result.success(output)
    }

}