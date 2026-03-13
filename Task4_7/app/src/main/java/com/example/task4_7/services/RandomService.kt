package com.example.task4_7.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RandomService : Service() {
    private var job: Job? = null

    inner class RandomBinder: Binder() {
        fun getService(): RandomService =  this@RandomService
    }

    private val binder = RandomBinder()

    override fun onBind(intent: Intent?): IBinder? {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(1000L)
                val random = (0..100).random()

                val broadcastIntent = Intent("RANDOM_UPDATE")
                broadcastIntent.setPackage(packageName)
                broadcastIntent.putExtra("number", random)
                sendBroadcast(broadcastIntent)
            }
        }
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}