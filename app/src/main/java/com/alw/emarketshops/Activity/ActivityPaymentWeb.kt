package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.net.UrlQuerySanitizer.ValueSanitizer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_payment_web.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


var charge_id = ""
class ActivityQrWeb : AppCompatActivity() {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private var shipping = ""
    private val currentDate  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_web)
        val i = intent
        val reference_order = i.getStringExtra("reference_order").toString()
        val amount = i.getStringExtra("amount").toString()
        this.shipping = i.getStringExtra("shipping").toString()
//        ActivitySelectPayment().creatOrderData(3,reference_order,"",shipping)


        val settings: WebSettings = webview.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowContentAccess = true
        settings.allowFileAccess = true


        webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {



                webview.loadUrl(
                    ("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('form')[0].innerHTML+'</html>');")
                )
                if (url == "https://www.emarketshops.com/kbankcard/request3.php") {
                    inquiry(charge_id)
                }


            }


        }
        webview.addJavascriptInterface(MyJavaScriptInterface(this), "HtmlViewer")

        val key = OrderAPI().pkey


        val url = "https://www.emarketshops.com/kbankcard/request1.php"
        val postData = "total=$amount"
        webview.postUrl(url, postData.toByteArray())

    }

    internal class MyJavaScriptInterface(ctx: Context) {
        private val ctx:Context = ctx

        @JavascriptInterface
        fun showHTML(html: String) {
            println(html)
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

    private fun setPaymentData(topic: OrderAPI.change){

        val list = hashMapOf(
            "payList" to listOf(topic)
        )
        val ref = db.collection("k_payment").document(uid!!)
        ref.get().addOnSuccessListener {
            if (it.data !== null){
                ref.update("payList", FieldValue.arrayUnion(topic))
            }else{
                ref.set(list)
                    .addOnSuccessListener {
                        Log.d("TAG", "Success insert Payment ")
                    }
            }
            creatOrderData(topic.reference_order, topic.id, shipping)
        }

    }

    fun creatOrderData(reference_order: String, order_id: String, shipping: String){
        println("creatOrderData>> $reference_order")
        val dbAddress = db.collection("userProfile").document(uid!!)
        dbAddress.get().addOnSuccessListener{
            val dataAddress = hashMapOf(
                "address" to "${it["address"].toString()} ${it["subdistrict"].toString()} " +
                        "${it["district"].toString()} ${it["province"].toString()}",
                "name" to it["receivename"].toString(),
                "phone" to it["phone"].toString(),
                "postcode" to it["zipcode"].toString()
            )


            val doc = db.collection("cart")
                .document(uid.toString())
            doc.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        val arrayList = ArrayList<Any>()
                        var itemdata: MutableMap<*, *>? = null
                        var totalCart = 0.00
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<*>
                            for (each in list) {
                                itemdata = each as MutableMap<*, *>?
                                if (itemdata != null && itemdata["isSelect"] == true) {
//                                println(itemdata["brandId"].toString() + itemdata["name"].toString())
                                    val price: String = itemdata["price"].toString()
                                    val qty: String = itemdata["qty"].toString()
                                    val itemOrder = hashMapOf(
                                        "qty" to qty,
                                        "total" to (price.toDouble() * qty.toDouble()).toString(),
                                        "subTotal" to (price.toDouble() * qty.toDouble()).toString(),
                                        "pricePerUnit" to price,
                                        "productName" to itemdata["name"].toString(),
                                        "productId" to itemdata["productId"].toString()
                                    )
                                    totalCart += (price.toDouble() * qty.toDouble())
                                    arrayList.add(itemOrder)
                                }

                            }
                            val paymentDetail = hashMapOf(
                                "total" to totalCart,
                                "shippingCost" to "0",
                                "paymentType" to "creditcard"
                            )
                            val data = hashMapOf(
                                "cancelDate" to "",
                                "customerId" to uid.toString(),
                                "insertDate" to Timestamp.now(),
                                "isActive" to true,
                                "orderItem" to arrayList,
                                "orderNo" to order_id,
                                "paymentDetail" to paymentDetail,
                                "processing" to "not processed",
                                "reference_order" to reference_order,
                                "sellerId" to itemdata?.get("brandId").toString(),
                                "serviceCost" to "0",
                                "shipping" to shipping,
                                "shippingAddress" to dataAddress,
                                "status" to "success",
                                "total" to totalCart,
                                "tracking" to "${ActivitySelectPayment().getRandomString(3)}${currentDate}TH"
                            )
                            println(data)
                            val orderList = hashMapOf(
                                "orderList" to listOf(data)
                            )

                            db.collection("orders").document(itemdata?.get("brandId").toString())
                                .get().addOnSuccessListener{
                                    if (it.data !== null) {
                                        db.collection("orders")
                                            .document(itemdata?.get("brandId").toString())
                                            .update("orderList", FieldValue.arrayUnion(data))
                                            .addOnSuccessListener {
                                                Log.d("TAG", "Success insert DataOrder: ")
                                            }
                                    }else{
                                        db.collection("orders")
                                            .document(itemdata?.get("brandId").toString())
                                            .set(orderList as Map<*, *>)
                                            .addOnSuccessListener {
                                                Log.d("TAG", "Success insert DataOrder: ")
                                            }
                                    }
                                    updateCartData(uid!!)
                                }
                        }



                    } else {
                        Toast.makeText(this, "no cart data", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
    fun updateCartData(uid: String){
        println("updateCartData>> $uid")
        db.collection("cart").document(uid)
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        val newList = ArrayList<Any>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<Any>
                            for (each in list) {
                                val itemdata: MutableMap<String, *>? = each as MutableMap<String, *>?
                                if (itemdata != null && itemdata["isSelect"] == false) {
                                    newList.add(itemdata)
                                }
                            }

                            val productList = hashMapOf(
                                "productlist" to newList
                            )

                            val ref1 = FirebaseFirestore.getInstance()
                            ref1.collection("cart").document(uid)
                                .set(productList)

                        }

                    } else {
                        Log.d("cart data", "no cart data")
                    }
                }


            }
    }
}

