package com.example.mylavanderiapp.core.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarManager : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        android.util.Log.d("CookieJar", "Guardando cookies para: ${url.host} -> ${cookies.map { it.name }}")
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = cookieStore[url.host] ?: emptyList()
        android.util.Log.d("CookieJar", "Cargando cookies para: ${url.host} -> ${cookies.map { it.name }}")
        return cookies
    }
}