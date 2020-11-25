package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_qr_web.*
import okhttp3.OkHttpClient
import java.util.*


class ActivityQrWeb : AppCompatActivity() {
    val client: OkHttpClient = OkHttpClient().newBuilder().build()
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_web)
        val i = intent
        val reference_order = i.getStringExtra("reference_order").toString()
        val shipping = i.getStringExtra("shipping").toString()
        val amount = i.getStringExtra("amount").toString()

//        ActivitySelectPayment().creatOrderData(3,reference_order,"",shipping)


        val settings: WebSettings = webview.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true

//        webview.loadData(embUri, "text/html", "UTF-8")

        webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
               println("onPageFinished>$url")

            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request != null) {
                    println("InterceptRequest>>${request.requestHeaders}")
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                println("shouldOverrideUrlLoading>>$url")
                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                println("shouldOverrideUrlLoading>>$request")
                return false
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                println("onLoadResource $url")
            }
        }

        val key = OrderAPI().pkey


        val url = "https://www.emarketshops.com/kbankcard/request1.php"
        val postData = "total=$amount"
        webview.postUrl(url, postData.toByteArray())

    }



}
