package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.R
import com.google.firebase.Timestamp
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_information.*
import java.util.HashMap

class ActivityInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        infor_email.setText(FirebaseController.Userdata.email)

        toolbarInformation.setOnClickListener {
            finish()
        }

        btnSendInformation.setOnClickListener {
            val currentDate =  Timestamp.now()
            val data = hashMapOf(
            "email" to infor_email.text.toString(),
            "phone" to infor_phone.text.toString(),
            "detail" to infor_detail.text.toString(),
            "time" to currentDate
            )

            val information
                    = hashMapOf(
                "information" to listOf(data)
            )



            db.collection("custumer_information").document(uid!!)
                .get().addOnSuccessListener {
                    println(it.data.toString())
                    if (it.data !== null) {
                        db.collection("custumer_information").document(uid!!)
                            .update("information" , FieldValue.arrayUnion(data))
                            .addOnSuccessListener {
                                val dialogBuilder = AlertDialog.Builder(this)
                                dialogBuilder.setTitle("ส่งข้อมูลเรียบร้อย")
                                dialogBuilder.setMessage("เราได้รับข้อมูลของคุแล้ว เราจะติดต่อกลับโดยเร็ว")
                                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                                    finish()
                                }
                                dialogBuilder.show()
                            }


                    } else {

                        db.collection("custumer_information").document(uid!!)
                            .set(information as Map<String, Any>)
                            .addOnSuccessListener {
                                val dialogBuilder = AlertDialog.Builder(this)
                                dialogBuilder.setTitle("ส่งข้อมูลเรียบร้อย")
                                dialogBuilder.setMessage("เราได้รับข้อมูลของคุแล้ว เราจะติดต่อกลับโดยเร็ว")
                                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->

                                    finish()
                                }
                                dialogBuilder.show()
                            }
                    }


                }



        }
    }
}