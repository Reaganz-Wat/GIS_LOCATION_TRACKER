package com.example.posapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AirPlaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                Log.d("", "")
            }
            Intent.ACTION_SCREEN_OFF -> {
                Log.d("", "")
            }
        }
    }
}