package com.alw.emarketshops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_re_quotation.*

class ActivityReQuotation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_quotation)

        btnSendReQuo.setOnClickListener {

            if (checkBoxAllow.isChecked){

                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("")
                dialogBuilder.setMessage("ส่งคำขอเสนอราเรียบร้อย")
                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                    finish()
                }
                dialogBuilder.show()
            }else{
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("")
                dialogBuilder.setMessage("กรุณายินยอมข้อตกลง")
                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                }
                dialogBuilder.show()
            }


        }
    }
}