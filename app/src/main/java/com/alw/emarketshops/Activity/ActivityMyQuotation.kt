package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterQuotationList
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelQuotationList
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_quotation.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityMyQuotation : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_quotation)
        getQuolist()

        myQuoToolbar.setOnClickListener { finish() }
    }

    @SuppressLint("SimpleDateFormat")
    fun getQuolist(){
    val db = FirebaseFirestore.getInstance()
        val doc = db.collection("quotation")
            .document(FirebaseController.Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null){
                    if (documentSnapshot.data !== null){
                        val arrayList = ArrayList<ModelQuotationList>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries){
                            val list = entry.value as ArrayList<Any>
                            for (each in list){
                                val quodata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                if (quodata !== null){
                                    val  quoNo:String = quodata["quotationNo"].toString()
                                    val  cate:String ="Category : "+ quodata["category"].toString()
                                    var product:String ="Sub Category : "+ quodata["subCategory"].toString()
                                    val timestamp = quodata["quotationDate"] as com.google.firebase.Timestamp
                                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                    val netDate = Date(milliseconds)
                                    val date ="Date : "+ sdf.format(netDate).toString()
                                    val description = "Des. : " +quodata.get("description").toString()

                                    val itemdata: Any? = quodata["quotationItem"]
                                    val ls = itemdata as ArrayList<Any>
                                    for (i in ls){
                                        val data: MutableMap<*, *>? = i as MutableMap<*, *>?
                                        val productname = data?.get("productName").toString()

                                        product += " / $productname"
                                    }

                                    arrayList.add(ModelQuotationList(quoNo,cate,product,date,description))
                                }
                            }

                            val adapter = AdapterQuotationList(arrayList,this)
                            recyclerViewQuolist.layoutManager =
                                GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                            recyclerViewQuolist.adapter = adapter
                        }
                    }
                }
        }
    }
}