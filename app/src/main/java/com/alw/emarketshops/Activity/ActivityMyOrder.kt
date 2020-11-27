package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.Model.ModelMyOrderList
import com.alw.emarketshops.R
import com.google.common.collect.Multimap
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_my_order.*

class ActivityMyOrder : AppCompatActivity() {
    val ref = db.collection("orders")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        getOrdersList()

        toolbarMyOrder.setOnClickListener {
            finish()
        }
    }



    private fun getOrdersList(){
        println("getOrdersList")
        ref.get().addOnCompleteListener {
            val arrList = ArrayList<ModelMyOrderList>()
           if (it.isSuccessful){

               for (doc in it.result!!){

                   val dt:Any? = doc["orderList"]
                   val ls = dt as ArrayList<Any>

                   for (each in ls){
                       val data: MutableMap<*, *>? = each as MutableMap<*, *>?
//                       println(data?.get("customerId"))
//                       println(data?.get("orderNo"))
                       println(data?.get("reference_order"))
                   }
               }
           }

        }
    }
}