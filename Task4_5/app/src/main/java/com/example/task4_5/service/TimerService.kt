package com.example.task4_5.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Timer

class TimerService: Service() {

    private var seconds = 0
    private val SERVICE_ID = 100
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Log.d("TIMER", "onCreate()")
        NotificationHelper.createNotificationChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        seconds = 0

        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.createMessage(this, seconds))

        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive){
                delay(1000L)
                seconds++
                updateNotification()
            }
        }
        return  START_STICKY
    }

    private fun updateNotification(){
        val notification = NotificationHelper.createMessage(this, seconds)
        getSystemService(NotificationManager::class.java)
            .notify(NotificationHelper.NOTIFICATION_ID, notification)

        val broadcastIntent = Intent("TIMER_UPDATE")
        broadcastIntent.setPackage(packageName)
        broadcastIntent.putExtra("seconds", seconds)
        sendBroadcast(broadcastIntent)
    }

    fun getSeconds() = seconds

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

}