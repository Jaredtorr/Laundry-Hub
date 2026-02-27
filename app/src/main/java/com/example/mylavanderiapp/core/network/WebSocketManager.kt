package com.example.mylavanderiapp.core.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.mylavanderiapp.BuildConfig
import com.example.mylavanderiapp.core.di.LaundryWebSocket
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

data class WebSocketNotification(
    val id: Int,
    val message: String,
    val type: String,
    val reservationId: Int?
)

@Singleton
class WebSocketManager @Inject constructor(
    @LaundryWebSocket private val okHttpClient: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private var shouldReconnect = true
    private val handler = Handler(Looper.getMainLooper())

    private val _notifications = MutableSharedFlow<WebSocketNotification>(extraBufferCapacity = 10)
    val notifications = _notifications.asSharedFlow()

    private val pingRunnable = object : Runnable {
        override fun run() {
            webSocket?.send("ping")
            Log.d("WebSocket", "Ping enviado")
            handler.postDelayed(this, 30_000)
        }
    }

    fun connect() {
        shouldReconnect = true
        createConnection()
    }

    private fun createConnection() {
        val request = Request.Builder().url(BuildConfig.WS_URL).build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Conectado")
                handler.post(pingRunnable)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Mensaje: $text")
                if (text == "pong") return
                try {
                    val notification = gson.fromJson(text, WebSocketNotification::class.java)
                    _notifications.tryEmit(notification)
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error al parsear: $e")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
                handler.removeCallbacks(pingRunnable)
                if (shouldReconnect) {
                    handler.postDelayed({ createConnection() }, 3_000)
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Desconectado: $reason code=$code")
                handler.removeCallbacks(pingRunnable)
                if (shouldReconnect && code != 1000) {
                    handler.postDelayed({ createConnection() }, 3_000)
                }
            }
        })
    }

    fun disconnect() {
        shouldReconnect = false
        handler.removeCallbacks(pingRunnable)
        webSocket?.close(1000, "Logout")
        webSocket = null
    }
}