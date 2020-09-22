package com.alw.emarketshops.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.StrictMode
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_select_payment.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit


class ActivitySelectPayment : AppCompatActivity() {
    private var client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        textTotalpay.text = intent.getStringExtra("total")
        btnConfirm_pay.setOnClickListener {
            if (radioBtnQr.isChecked) {
                okHTTP(intent.getStringExtra("total"))
            }
            if (radioBtnCreditcard.isChecked){
                println("radioBtnCreditcard.isChecked")

            }
        }
        radioBtnQr.setOnClickListener {
            radioBtnBankpay.isChecked = !radioBtnQr.isChecked
            radioBtnCreditcard.isChecked = !radioBtnQr.isChecked


        }
        radioBtnBankpay.setOnClickListener {
            radioBtnQr.isChecked = !radioBtnBankpay.isChecked
            radioBtnCreditcard.isChecked = !radioBtnBankpay.isChecked
        }
        radioBtnCreditcard.setOnClickListener {
            radioBtnQr.isChecked = !radioBtnCreditcard.isChecked
            radioBtnBankpay.isChecked = !radioBtnCreditcard.isChecked


        }
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }



    fun postJson_qr(amount: String):String {
        return ("{amount:$amount,"
                + "currency:THB,"
                + "description:TESTPRODUCT,"
                + "source_type:qr,"
                + "reference_order:ALW1234567,"
                + "metadata:[]"
                + "}")
    }
    val currentDate  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    val reference_order = "ALW$currentDate"
    fun okHTTP(amount: String){
        val client = OkHttpClient().newBuilder().build()

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(
            mediaType,
            "{\r\n \"amount\": $amount," +
                    "\r\n \"currency\": \"THB\"," +
                    "\r\n \"description\": \"TESTPRODUCT\"," +
                    "\r\n \"source_type\": \"qr\"," +
                    "\r\n \"reference_order\": \"$reference_order\"," +
                    "\r\n \"metadata\":[]\r\n}"
        )
        val request = Request.Builder()
            .url(OrderAPI().url+"/qr/v2/order")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body()!!.string()
            val topic = Gson().fromJson(responseData, OrderAPI.Json4Kotlin_Base::class.java)
            println(topic.id)
            qrCheckout(topic.id, amount)
        }

    }
    fun qrCheckout(id: String, amount: String){

        val client = OkHttpClient().newBuilder().build()
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(
            mediaType,
            "{\r\n \"order_id\": \"$id\"," +
                    "\r\n \"amount\": \"$amount\"," +
                    "\r\n \"currency\": \"THB\"," +
                    "\r\n \"description\": \"TESTPRODUCT\"," +
                    "\r\n \"sof\": \"ThaiQR\"," +
                    "\r\n \"reference_order\": \"$reference_order\"," +
                    "\r\n \"metadata\":[]\r\n}"
        )
        val request = Request.Builder()
            .url(OrderAPI().url+"/qr/v2/qr")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body()!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.qrResponse::class.java)
            println(topic.id)

        val inten = Intent(this, ActivityQrthai::class.java)
            inten.putExtra("response", responseData)
            inten.putExtra("amount", amount)
            inten.putExtra("order_id", id)
            startActivity(inten)
            finish()
        }
    }

}


