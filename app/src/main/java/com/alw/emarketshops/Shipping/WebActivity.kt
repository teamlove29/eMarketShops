package com.alw.emarketshops.Shipping

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val settings: WebSettings = inter_web.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true


        inter_web.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {



                inter_web.loadUrl(
                    ("javascript:HtmlViewer.showHTML" +
                            "('<html>'+'</html>');")
                )

            }


        }
        inter_web.addJavascriptInterface(MyJavaScriptInterface(this), "HtmlViewer")


        val url = "https://www.emarketshops.com/kbankcard/inter.php"
        inter_web.loadUrl(url)

    }

    internal class MyJavaScriptInterface(ctx: Context) {
        private val ctx:Context = ctx

        @JavascriptInterface
        fun showHTML(html: String) {
            println(html)

        }
    }

}