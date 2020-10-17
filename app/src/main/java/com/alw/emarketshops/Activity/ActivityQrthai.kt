package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Images
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_qrthai.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*
import java.util.concurrent.TimeUnit


class ActivityQrthai : AppCompatActivity() {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val mediaType = "application/json".toMediaTypeOrNull()
    private var order_id = ""
    private var reference_order =""
    private var shipping = ""
    private var timer: CountDownTimer? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrthai)
        val i = intent
        shipping = i.getStringExtra("shipping").toString()
        order_id = i.getStringExtra("order_id").toString()
        reference_order = i.getStringExtra("reference_order").toString()



        val topic = Gson().fromJson(
            i.getStringExtra("response").toString(),
            OrderAPI.qrResponse::class.java
        )
        val base64string = topic.image_with_base64

        val decodedString: ByteArray = Base64.getDecoder().decode(base64string)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        imageViewDinamicQR.setImageBitmap(decodedByte)
        textId.text = topic.id
        textName.text = topic.account_name
        textRefnumber.text = topic.created
        textAmount.text = i.getStringExtra("amount")

        val timer = object : CountDownTimer(topic.expire_time_seconds.toLong() * 1000, 100) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                textTime.text = String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
            }

            override fun onFinish() {
                cancelQrTransaction(topic.id)
                finish()
            }
        }
        timer.start()
        timerCheck()

        btnSaveQrImg.setOnClickListener {
            imageViewDinamicQR.setDrawingCacheEnabled(true)
            val b: Bitmap = imageViewDinamicQR.drawable.toBitmap()
            Images.Media.insertImage(this.contentResolver, b, "ThaiQR$topic.created", topic.created)

            Toast.makeText(this, "Save img QR", Toast.LENGTH_SHORT).show()
        }
        btnCancelTran.setOnClickListener {
            cancelQrTransaction(topic.id)
        }
        btnCheckStatus.setOnClickListener {
            if(!inquiryQR(order_id)){
                Toast.makeText(this, "Payment not success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cancelQrTransaction(qr_id: String) {

        val body = RequestBody.create(mediaType, "")
        val request = Request.Builder()
            .url(OrderAPI().url + "/qr/v2/qr/$qr_id/cancel")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            println(responseData)
            if (timer !== null){
            (timer as CountDownTimer).cancel()
            }
            finish()
        }
    }

    fun inquiryOrder(order_id: String){
        val body = RequestBody.create(mediaType, "")
        val request = Request.Builder()
            .url(OrderAPI().url + "/qr/v2/order/$order_id/cancel")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.inquiryOrder::class.java)
            println(topic.status)
            if (topic.status == "success"){

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("การชำระเงิน")
                dialogBuilder.setMessage("ชำระเงินเรียบร้อย")
                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                    finish()
                }
                dialogBuilder.show()
            }else{
                println("not success")
            }
        }
    }

    fun inquiryQR(order_id: String):Boolean{
        println(order_id)
        var success = true
        val request = Request.Builder()
            .url(OrderAPI().url + "/qr/v2/qr/$order_id")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.Qr_inquiry::class.java)
            println(topic.status)
            if (topic.status == "success"){
//                updateOrderStatus()
                ActivitySelectPayment().creatOrderData(1,reference_order,order_id,shipping)
                success = true
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("การชำระเงิน")
                dialogBuilder.setMessage("ชำระเงินเรียบร้อย")
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

    private fun updateOrderStatus() {


    }


    fun timerCheck(){
        val _timer: CountDownTimer = object: CountDownTimer(10000, 1) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
              timer = object: CountDownTimer(1000000, 30000) {
                    override fun onTick(millisUntilFinished:Long) {
                        println("timerCheck start>>>>")
                        if (inquiryQR(order_id)){
                            cancel()
                        }
                    }
                    override fun onFinish() {
                        println("timerCheck Finish>>")
                        cancel()
                    }
                }
                (timer as CountDownTimer).start()
            }
        }
        _timer.start()

    }
}