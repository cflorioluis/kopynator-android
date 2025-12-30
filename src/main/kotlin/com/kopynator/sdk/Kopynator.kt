package com.kopynator.sdk

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class Kopynator private constructor(
    private val apiKey: String,
    private val baseUrl: String = "https://api.kopynator.com",
    private var locale: String = "en"
) {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val translations = ConcurrentHashMap<String, String>()

    companion object {
        @Volatile
        private var instance: Kopynator? = null

        fun initialize(apiKey: String, locale: String = "en"): Kopynator {
            return instance ?: synchronized(this) {
                instance ?: Kopynator(apiKey, locale = locale).also { instance = it }
            }
        }

        fun getInstance(): Kopynator {
            return instance ?: throw IllegalStateException("Kopynator must be initialized first")
        }
    }

    fun setLocale(newLocale: String) {
        this.locale = newLocale
    }

    fun t(key: String, defaultValue: String = key): String {
        return translations[key] ?: defaultValue
    }

    fun fetchTranslations(onComplete: (Boolean) -> Unit) {
        val url = "$baseUrl/tokens/fetch?token=$apiKey&langs=$locale"
        val request = Request.Builder()
            .url(url)
            .addHeader("x-kopynator-version", "1.2.0")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onComplete(false)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        if (body != null) {
                            try {
                                val mapType = object : TypeToken<Map<String, String>>() {}.type
                                val newTranslations: Map<String, String> = gson.fromJson(body, mapType)
                                translations.clear()
                                translations.putAll(newTranslations)
                                onComplete(true)
                                return
                            } catch (e: Exception) {
                                // Silent fail for malformed JSON
                            }
                        }
                    }
                    onComplete(false)
                }
            }
        })
    }
}
