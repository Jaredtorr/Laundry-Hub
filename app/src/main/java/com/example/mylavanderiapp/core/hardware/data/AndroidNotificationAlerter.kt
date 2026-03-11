package com.example.mylavanderiapp.core.hardware.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.RingtoneManager
import android.os.*
import androidx.core.app.NotificationCompat
import com.example.mylavanderiapp.LaundryHiltApp.Companion.CHANNEL_ID
import com.example.mylavanderiapp.MainActivity
import com.example.mylavanderiapp.R
import com.example.mylavanderiapp.core.hardware.domain.NotificationAlerter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

class AndroidNotificationAlerter @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationAlerter {

    private val cameraManager      = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val mainHandler         = Handler(Looper.getMainLooper())

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private val cameraId: String? by lazy {
        try { cameraManager.cameraIdList.firstOrNull() } catch (e: Exception) { null }
    }

    private var notificationId = 0

    override fun hasFlash(): Boolean =
        context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_FLASH)

    override fun alert(title: String, message: String) {
        mainHandler.post {
            playNotificationSound()
            vibrate()
            showSystemNotification(title, message)
        }
        CoroutineScope(Dispatchers.IO).launch { blink() }
    }

    override fun showSystemNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId++, notification)
    }

    override fun vibrate() {
        mainHandler.post {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(0, 300, 100, 300),
                            -1
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 300, 100, 300), -1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun playNotificationSound() {
        try {
            val uri      = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun blink() {
        if (!hasFlash()) return
        try {
            repeat(3) {
                setFlash(true)
                delay(100)
                setFlash(false)
                delay(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setFlash(enabled: Boolean) {
        cameraId?.let {
            try { cameraManager.setTorchMode(it, enabled) } catch (e: Exception) { e.printStackTrace() }
        }
    }
}