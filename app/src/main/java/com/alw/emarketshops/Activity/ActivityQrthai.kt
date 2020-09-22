package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_qrthai.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*
import java.util.concurrent.TimeUnit


class ActivityQrthai : AppCompatActivity() {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val mediaType = MediaType.parse("application/json")
    private  var  order_id = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrthai)
        val i = intent
        order_id = i.getStringExtra("order_id").toString()
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
            inquiryQR(order_id)
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
            val responseData = response.body()!!.string()
            println(responseData)
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
            val responseData = response.body()!!.string()
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

    fun inquiryQR(order_id: String){
        println(order_id)
        val request = Request.Builder()
            .url(OrderAPI().url + "/qr/v2/qr/$order_id")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()

        client.newCall(request).execute().use { response ->
            val responseData = response.body()!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.Qr_inquiry::class.java)
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
                Toast.makeText(this, "Payment not success", Toast.LENGTH_SHORT).show()
                println("not success")
            }
        }
    }

    fun timerCheck(){
        val _timer: CountDownTimer = object: CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val timer: CountDownTimer = object: CountDownTimer(1000000, 30000) {
                    override fun onTick(millisUntilFinished:Long) {
                        println("start>>>>")
                        inquiryQR(order_id)
                    }
                    override fun onFinish() {
                        println("Finish>>")
                    }
                }
                timer.start()
            }
        }
        _timer.start()

    }
}