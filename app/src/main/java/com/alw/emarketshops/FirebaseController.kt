package com.alw.emarketshops

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterItemList
import com.alw.emarketshops.Model.ModelUser
import com.alw.emarketshops.Fragment.CartFragment
import com.alw.emarketshops.Fragment.MessageFragment
import com.alw.emarketshops.Model.ModelItemCartList
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat


class FirebaseController {
    object  Userdata {
        val modelUser = ModelUser()
        var uid = modelUser.user?.uid
        var name = modelUser.user?.displayName
    }
    private var taskShop: Task<DocumentSnapshot>? = null
    val db = FirebaseFirestore.getInstance()
    val  docCart = "cart"


    fun getSetCartdata(qty: Int, position: Int,context: CartFragment){
        println("getSetCartdata")
        var total:Long=0
        db.collection(docCart).document(Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null) {
                    val map: MutableMap<*, *>? = documentSnapshot.data
                    for (entry in map!!.entries) {
                        val list = entry.value as ArrayList<Any>
                        val newList = ArrayList<Any>()
                        for ((index, each) in list.withIndex()) {
                            val itemdata: MutableMap<String, String>? = each as MutableMap<String, String>?
                            println(itemdata.toString())
                            if (itemdata != null) {

                                if (index == position){
                                    itemdata["qty"] = qty.toString()
                                }
                                newList.add(itemdata)
                                val price: String = itemdata["price"].toString()
                                val nqty: String = itemdata["qty"].toString()
                                total += (price.toLong() * nqty.toLong())
                            }
                        }
                        if (qty == 0){
                            newList.removeAt(position)
                        }
                        val productList = hashMapOf(
                            "productlist" to newList
                        )
                        val dec = DecimalFormat("#,###.00")
                        context.textsubTotalCart.text = dec.format(total)
                        val ref1 = FirebaseFirestore.getInstance()
                        ref1.collection(docCart).document(Userdata.uid.toString())
                            .set(productList)
                            .addOnSuccessListener {
                                context.getCartdata()
                            }

                    }

                } else {
                    Log.d("cart data", "no cart data")
                }
            }


        }

    }

    fun getProductData(productId: String): Task<DocumentSnapshot>? {
        var task: Task<DocumentSnapshot>? = null
        db.collection("product").document(productId)
            .get()
            .addOnCompleteListener {
                task = it
                val map: MutableMap<*, *>? = it.result!!.data  // shipping data
                if (map != null) {
                    val list: Any? = map["shipping"]
                    if (list != null) {
//                        shipping = list
                    }
                }


            }
        return  task
    }

    fun getShopData(userId: String):Task<DocumentSnapshot>? {

        val task = db.collection("shops")
            .document(userId)
            .get().addOnSuccessListener {
             }
      return task
    }

    fun updateUserData(name:String, uid:String){
        db.collection("userProfile").document(uid)
            .get().addOnSuccessListener{
                if (it.data == null){
                    val data = hashMapOf("name" to name)
                    db.collection("userProfile").document(uid)
                        .set(data)
                }else{
                    Log.d("updateUserDate >>","IsRady User")
                    updateShopData(name,uid)
                }
            }
    }

    private fun updateShopData(name:String, uid:String){
        db.collection("shops").document(uid)
            .get().addOnSuccessListener{
                if (it.data == null){
                    val data = hashMapOf("shopName" to name)
                    db.collection("shops").document(uid)
                        .set(data)
                }else{
                    Log.d("updateShopData >>","IsRady shop")
                }
            }
    }


}