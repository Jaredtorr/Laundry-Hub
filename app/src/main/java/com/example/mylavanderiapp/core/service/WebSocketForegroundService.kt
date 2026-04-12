package com.example.mylavanderiapp.core.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.mylavanderiapp.LaundryHiltApp.Companion.CHANNEL_ID
import com.example.mylavanderiapp.MainActivity
import com.example.mylavanderiapp.R
import com.example.mylavanderiapp.core.network.WebSocketManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketForegroundService : Service() {

    @Inject
    lateinit var webSocketManager: WebSocketManager

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP  = "ACTION_STOP"
        const val NOTIFICATION_ID = 1001

        fun startIntent(context: Context) =
            Intent(context, WebSocketForegroundService::class.java)
                .apply { action = ACTION_START }

        fun stopIntent(context: Context) =
            Intent(context, WebSocketForegroundService::class.java)
                .apply { action = ACTION_STOP }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(
                        NOTIFICATION_ID,
                        buildNotification(),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                    )
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification())
                }
                webSocketManager.connect()
            }
            ACTION_STOP -> {
                webSocketManager.disconnect()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        webSocketManager.disconnect()
        super.onDestroy()
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Laundry Hub activo")
            .setContentText("Recibirás alertas de tus lavadoras en tiempo real")
            .setOngoing(true)           // no se puede deslizar para cerrar
            .setSilent(true)            // no hace sonido ni vibra
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()
    }
}