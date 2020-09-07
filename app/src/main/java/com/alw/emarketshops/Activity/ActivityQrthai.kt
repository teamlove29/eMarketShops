package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_qrthai.*
import java.util.concurrent.TimeUnit


class ActivityQrthai : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrthai)

        val timer = object: CountDownTimer(120000, 100) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                textTime.text = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                )
            }
            override fun onFinish() {
                finish()
            }
        }
        timer.start()

        btnSaveQrImg.setOnClickListener {
            Toast.makeText(this, "Save img QR", Toast.LENGTH_SHORT).show()
        }
    }
}