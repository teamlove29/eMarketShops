package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_address_web.*

class ActivityAddress_web : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_web)

        val settings: WebSettings = web_address.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true
        web_address.loadUrl("file:///android_asset/address.html")
    }
}