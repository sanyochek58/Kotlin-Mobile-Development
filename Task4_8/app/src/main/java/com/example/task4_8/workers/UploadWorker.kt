package com.example.task4_8.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class UploadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        val file = inputData.getString("file") ?: Result.failure()

        delay(9000L)

        val output = workDataOf("file" to file)
        return Result.success(output)
    }

}