package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.android.gms.common.api.Api
import kotlinx.android.synthetic.main.activity_qr_web.*
import okhttp3.OkHttpClient


class ActivityQrWeb : AppCompatActivity() {
    val client: OkHttpClient = OkHttpClient().newBuilder().build()
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_web)
        val i = intent
        val reference_order = i.getStringExtra("reference_order").toString()
        val amount = "1.00" //i.getStringExtra("amount").toString()

        ActivitySelectPayment().creatOrderData(3,reference_order,"")


        val settings: WebSettings = webview.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true

//        webview.loadData(embUri, "text/html", "UTF-8")

        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
//               println("onPageFinished>$url")

            }

            override fun shouldInterceptRequest(view: WebView?,url: String?): WebResourceResponse? {
//                println("shouldInterceptRequest>$url")

                return null
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request != null) {
//                    println("InterceptRequest>>${request.requestHeaders}")
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                println("shouldOverrideUrlLoading>>$url")
                return false
            }
        }

        val key = OrderAPI().pkey
        webview.loadUrl("file:///android_asset/visa_emb.html?k=$key&a=$amount")
    }


}
