package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterItemList
import com.alw.emarketshops.Adapter.AdapterMyOrderList
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelMyOrderList
import com.alw.emarketshops.R
import com.google.common.collect.Multimap
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.fragment_cart.*

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
               var price:String
               var qty:String
               var sellerId :String
               var shipping:String
               var productId : String
               for (doc in it.result!!){
                   val dt:Any? = doc["orderList"]
                   val ls = dt as ArrayList<Any>
                   for (each in ls){
                       val data: MutableMap<*, *>? = each as MutableMap<*, *>?
                       if (data?.get("customerId") == uid) {
                           sellerId = data?.get("sellerId").toString()
                           val itemlist = data?.get("orderItem") as ArrayList<Any>
                           for (dt in itemlist){
                               val data_item: MutableMap<*, *>? = dt as MutableMap<*, *>?
                               productId = data_item?.get("productId").toString()
                               price = data_item?.get("pricePerUnit").toString()
                               qty = data_item?.get("qty").toString()
                               shipping = data?.get("shipping").toString()
                               arrList.add(ModelMyOrderList(sellerId,productId,qty,price,shipping))
                           }
                       }
                   }
               }

               val adapter = AdapterMyOrderList(arrList,this)
               myOrderListView.layoutManager =
                   GridLayoutManager(this, 1,
                       GridLayoutManager.VERTICAL, false)
               myOrderListView.adapter = adapter
           }

        }
    }
}