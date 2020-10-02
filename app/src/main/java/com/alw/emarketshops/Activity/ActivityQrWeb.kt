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

//        ActivitySelectPayment().creatOrderData(3,reference_order)

//        val inline = "<html>" +
//                "<body>" +
//                "<form method=\"POST\" action=\"/checkout\">\n" +
//                "<script \n" +
//                "src=\"https://dev-kpaymentgateway.kasikornbank.com/ui/v2/kinlinepayment.min.js\"\n" +
//                "data-apikey=\"${OrderAPI().skey}\"\n" +
//                "data-lang=\"EN\"\n" +
//                "data-write-log=\"false\">\n" +
//                "</script>\n" +
//                "<button>Submit</button>\n" +
//                "</form>" +
//                "</body>" +
//                "</html>"

//        val embUri = "<html>"+
//                "<meta charset=\"UTF-8\">\n" +
//                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
//                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +
//                "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Raleway\">"+
//                "<body>" +
//                "<center>"+
//                "<div class=\"w3-display-middle\">" +
//                "<h1 class=\"w3-large w3-animate-top\">Payment</h1>\n" +
//                "<h1 class=\"w3-big w3-center\">Total : $amount </h1>"+
//                "<form method=\"POST\" action=\"android_asset/index.html\">\n" +
//                "<script type=\"text/javascript\"\n" +
//                "src=\"https://dev-kpaymentgateway.kasikornbank.com/ui/v2/kpayment.min.js\"\n" +
//                "data-apikey=\"${OrderAPI().pkey}\"\n" +
//                "data-amount=\"$amount\"\n" +
//                "data-currency=\"THB\"\n" +
//                "data-payment-methods=\"card\"\n" +
//                "data-name=\"eMarketShops\"\n" +
//                "data-mid=\"401498309148001\">\n" +
//                "</script>\n" +
//                "</form>"+
//                "</div>"+
//                "</center>"+
//                "</body>" +
//                "</html>"

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
                println("shouldInterceptRequest>$url")

                return null
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

        }

        val key = OrderAPI().pkey
        webview.loadUrl("file:///android_asset/visa_emb.html?k=$key&a=$amount")
    }


}
