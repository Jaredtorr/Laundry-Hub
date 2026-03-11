package com.example.mylavanderiapp.core.hardware.domain

interface NotificationAlerter {
    fun alert(title: String, message: String)
    fun vibrate()
    fun hasFlash(): Boolean
    fun showSystemNotification(title: String, message: String)
}