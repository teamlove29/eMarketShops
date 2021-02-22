package com.alw.emarketshops.Shipping

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class InterShipping {

    fun auth_token() {
        val client = OkHttpClient().newBuilder()
            .build()

        val request = Request.Builder()
            .url("https://www.cash-pos.com/seller/getShippingToken")
            .method("LINK", null)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body!!.string()
            val topic = Gson().fromJson(
                responseData,
                AuthResponse::class.java
            )
            println("accessToken = ${topic.accessToken}")
            println("refreshToken = ${topic.refreshToken}")

//            refresh_token(topic.refreshToken)
        }

    }
    fun refresh_token(refreshToken : String) {
        val client = OkHttpClient().newBuilder()
            .build()

        val request = Request.Builder()
            .url("https://www.cash-pos.com/seller/getShippingRefreshToken/$refreshToken")
            .method("LINK", null)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body!!.string()
            val topic = Gson().fromJson(
                responseData,
                AuthResponse::class.java
            )
            println("refreshToken2 = ${topic.refreshToken}")
        }

    }
    data class AuthResponse(
        val ielApiKey: String,
        val accessToken: String,
        val refreshToken: String,
        val expiresDate: String
    )
}