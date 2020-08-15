package com.alw.emarketshops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_payment.*

class ActivitySelectPayment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        textTotalpay.text = intent.getStringExtra("total")
        btnConfirm_pay.setOnClickListener {
            if (radioBtnQr.isChecked) {
                val intent = Intent(this, ActivityQrthai::class.java)
                startActivity(intent)
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


}