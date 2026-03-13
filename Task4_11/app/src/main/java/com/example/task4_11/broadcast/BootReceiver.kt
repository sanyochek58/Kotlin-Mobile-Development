package com.example.task4_11.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val ctx = context ?: return
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (AlarmScheduler.isEnabled(ctx)) {
                AlarmScheduler.schedule(ctx)
            }
        }
    }
}