package com.alw.emarketshops.Activity

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_qr_web.*
import java.util.*


class ActivityQrWeb : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_web)
        val i = intent
        val order_id = i.getStringExtra("id")
        val base64string =i.getStringExtra("base64")

        val decodedString: ByteArray = Base64.getDecoder().decode(base64string)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        imageViewQR.setImageBitmap(decodedByte)

    }
}