package com.example.task4_6.service;

import com.example.task4_6.R
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService: Service() {

    private var job: Job? = null
    private val SERVICE_ID = 100

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
        val seconds = intent?.getIntExtra("seconds", 0) ?: 0

        job = CoroutineScope(Dispatchers.Default).launch {
            for (i in seconds downTo 1){
                val broadcastIntent = Intent("TIMER_UPDATE")
                broadcastIntent.setPackage(packageName)
                broadcastIntent.putExtra("seconds",i)
                sendBroadcast(broadcastIntent)

                delay(1000L)
            }
            showFinishNotification()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun showFinishNotification(){
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Таймер завершён !")
            .setContentText("Время вышло!")
            .build()

        getSystemService(NotificationManager::class.java).notify(SERVICE_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
