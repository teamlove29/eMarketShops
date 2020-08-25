package com.alw.emarketshops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_re_quotation.*

class ActivityReQuotation : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_quotation)

        getCategoryList()
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

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getcategoryTypelist(spinnerCategory.getSelectedItem().toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }
    fun getCategoryList(){
        db.collection("category")
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<String>()
               if (task.isSuccessful){
                   for (doc in task.result!!){

                       Log.d("task.result",task.result.toString())
                       val code : String = doc["cateCode"].toString()
                       val name : String = doc["nameTH"].toString()

                       newArrayList.add(name)
                   }
               }
                val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, newArrayList)
                array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory!!.adapter = array_adapter

            }
    }
    fun getcategoryTypelist(name:String){
        db.collection("category").whereEqualTo("nameTH",name)
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<String>()
                if (task.isSuccessful){
                    for (doc in task.result!!){

                    }

                }

            }
    }
}