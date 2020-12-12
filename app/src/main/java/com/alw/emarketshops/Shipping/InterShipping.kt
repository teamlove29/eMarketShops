package com.alw.emarketshops.Shipping

import com.alw.emarketshops.OrderAPI
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class InterShipping {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val mediaType = "application/json".toMediaTypeOrNull()
    private val ielApiKey = ""
    val ielApiSecret = ""
    private val url = "https://sandbox.iel.co.th/v1/auth/token"
    fun auth_token(){
        val body = "{ielApiSecret: $ielApiSecret}".toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-IEL-KEY",ielApiKey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            val topic = Gson().fromJson(responseData,
                auth_response::class.java)
            println(topic.accessToken)

        }
    }

    data class auth_response(
        val ielApiKey : String,
        val accessToken : String,
        val refreshToken : String,
        val expiresDate : String
    )
}