package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterQuotationList
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelQuotationList
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_quotation.*
import kotlinx.android.synthetic.main.activity_quotation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityQuotation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotation)

       val quotationNo = intent.getStringExtra("quotationNo")
        getQuotation(quotationNo)

        toolbarQuota.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun getQuotation(quotationNo:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("quotation")
            .document(uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null){
                    if (documentSnapshot.data !== null){
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries){
                            val list = entry.value as ArrayList<Any>
                            for (each in list){
                                val quodata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                if (quodata !== null) {
                                    if (quodata["quotationNo"].toString() == quotationNo) {
                                        txtRefNo.text =
                                            quodata?.get("quotationNo")?.toString()
                                        txtCategory.text =
                                            quodata?.get("category")?.toString()
                                        txtSubCategory.text =
                                            quodata?.get("subCategory")?.toString()
                                        val timestamp =
                                            quodata["quotationDate"] as com.google.firebase.Timestamp
                                        val milliseconds =
                                            timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                        val netDate = Date(milliseconds)
                                        txtDate.text = "Date : " + sdf.format(netDate).toString()
                                        txtSubject.text = "Des. : " + quodata["subject"].toString()
                                        txtSubtotal.text = quodata["subtotal"].toString()
                                        val itemdata: Any? = quodata["quotationItem"]
                                        val ls = itemdata as ArrayList<Any>
                                        for (i in ls) {
                                            val data: MutableMap<*, *>? = i as MutableMap<*, *>?

                                            txtProductName.text = data?.get("productName").toString()
                                            txtProductPrice.text = data?.get("priceUnit").toString()
                                            txtProductQty.text = data?.get("qty").toString()
                                            txtShipping.text = data?.get("shipping").toString()
                                            val task = FirebaseController()
                                                .getShopData(quodata["sellerId"] as String)
                                            task?.addOnSuccessListener {
                                                val ct :MutableMap<*, *>? = it["contact"] as MutableMap<*, *>?
                                                println(ct?.get("phone"))

                                                txtShopName.text = it["shopName"].toString()
                                                txtContact.text = "${ct?.get("address")} " +
                                                        "${ct?.get("subDistrict")} " +
                                                        "${ct?.get("district")} " +
                                                        "${ct?.get("province")} " +
                                                        "${ct?.get("phone")}"
                                            }

                                        }

                                    }else{
                                        println(quotationNo)
                                    }
                                }
                            }


                        }
                    }
                }
            }
    }
}