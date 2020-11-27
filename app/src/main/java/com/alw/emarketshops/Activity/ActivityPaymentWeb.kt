package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_payment_web.*
import okhttp3.OkHttpClient
import okhttp3.Request

var charge_id = ""
class ActivityQrWeb : AppCompatActivity() {
    val client: OkHttpClient = OkHttpClient().newBuilder().build()

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_web)
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


        webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
               println("onPageFinished>$url")
                webview.loadUrl(("javascript:HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"))
                if (url == "https://www.emarketshops.com/kbankcard/request3.php") {
                    inquiry(charge_id)
                }
            }


        }
        webview.addJavascriptInterface(MyJavaScriptInterface(this), "HtmlViewer")

        val key = OrderAPI().pkey


        val url = "https://www.emarketshops.com/kbankcard/request1.php"
        val postData = "total=$amount&reference_order=$reference_order&uid=$uid"
        webview.postUrl(url, postData.toByteArray())

    }

    internal class MyJavaScriptInterface(ctx:Context) {
        private val ctx:Context = ctx

        @JavascriptInterface
        fun showHTML(html:String) {
            charge_id = html.substringAfter("\"objectId\" value=\"")
                .substringBefore("\">")
        }
    }

    fun inquiry(charge_id: String):Boolean{
        var success = true
        val request = Request.Builder()
            .url(OrderAPI().url + "/card/v2/charge/$charge_id")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.change::class.java)
            println(topic.status)
            if (topic.status == "success"){
                setPaymentData(topic)
                val dialogBuilder = AlertDialog.Builder(this@ActivityQrWeb)
                    dialogBuilder.setTitle("ชำระเงิน")
                    dialogBuilder.setMessage("การชำระเงินเสร็จสิ้น")
                    dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                        finish()
                    }
                    dialogBuilder.show()
            }else{
                println("not success")
                success = false
            }
        }
        return success
    }

    private fun setPaymentData(topic:OrderAPI.change){
        val list = hashMapOf(
            "payList" to listOf(topic)
        )
        db.collection("k_payment").document(uid!!)
            .set(list as Map<*,*>)
            .addOnSuccessListener {
                Log.d("TAG", "Success insert Payment: ")
            }
    }
}

