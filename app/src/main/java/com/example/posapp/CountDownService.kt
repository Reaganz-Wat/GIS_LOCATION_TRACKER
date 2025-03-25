package com.example.posapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CountDownService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Services created: ", "Created Service now")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread {
            for (i in 1..10) {
                Thread.sleep(1000)
                Log.d("Output: ", "running task ${i}")
            }
            stopSelf()
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroyed Service", "Destroyed services")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}