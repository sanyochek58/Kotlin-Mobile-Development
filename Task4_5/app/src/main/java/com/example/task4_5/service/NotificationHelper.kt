package com.example.task4_5.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

import androidx.core.app.NotificationCompat
import com.example.task4_5.R
import kotlin.time.Duration.Companion.seconds


object NotificationHelper {

    const val CHANNEL_ID = "timer_channel"
    const val NOTIFICATION_ID = 100
    private const val CHANNEL_NAME = "Timer"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context){
        val notificationManager: NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val chanel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(chanel)
    }

    fun createMessage(context: Context, seconds: Int) = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Таймер работает!")
        .setContentText("Прошло ${seconds} секунд")
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .build()
}