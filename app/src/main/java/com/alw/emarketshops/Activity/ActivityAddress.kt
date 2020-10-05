package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_address.*
import java.util.HashMap

class ActivityAddress : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        loadAddressData()
        println(uid.toString())
        toolbarAddress.setOnClickListener {
            this.finish()
        }
        btnSaveaddress.setOnClickListener {
            saveAddressdatat()
        }
    }

    private fun saveAddressdatat(){
//        val data: MutableMap<String, Any> = hashMapOf(
//            "brandId" to brandId

        val address: MutableMap<String, Any> = hashMapOf(
        "recivename" to edtName.text.toString(),
        "phone" to edtPhone.text.toString(),
        "address" to edtAddress.text.toString(),
        "province" to edtProvince.text.toString(),
        "district" to edtDistrict.text.toString(),
        "subdistrict" to edtSubdistrict.text.toString(),
        "zipcode" to edtZipcode.text.toString()
        )


        db.collection("userProfile").document(uid.toString())
            .update(address as Map<String, Any>)
            .addOnSuccessListener {
                Log.d("TAG", "Success update Data: ")
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("")
                dialogBuilder.setMessage("บันทึกข้อมูลเรียบร้อย")
                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
//                    finish()
                }
                dialogBuilder.show()
            }
            .addOnFailureListener{
                Log.d("TAG", "update Data error: ")
            }


    }

    private fun loadAddressData(){
        db.collection("userProfile").document(uid.toString())
            .get()
            .addOnSuccessListener{
                val map: MutableMap<*, *>? = it.data
                println(map?.get("phone").toString())
                edtName.setText(map?.get("recivename").toString())
                edtAddress.setText(map?.get("address").toString())
                edtPhone.setText(map?.get("phone").toString())
                edtProvince.setText(map?.get("province").toString())
                edtDistrict.setText(map?.get("district").toString())
                edtSubdistrict.setText(map?.get("subdistrict").toString())
                edtZipcode.setText(map?.get("zipcode").toString())

            }
    }
}