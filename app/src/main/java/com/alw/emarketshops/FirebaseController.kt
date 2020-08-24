package com.alw.emarketshops

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.alw.emarketshops.ui.ModelUser
import com.alw.emarketshops.ui.cart.CartFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseController {
    object  Userdata {
        val modelUser = ModelUser()
        var uid = modelUser.user?.uid
        var name = modelUser.user?.displayName
    }
    object total{
        var totalCart:String="0"
    }
    val db = FirebaseFirestore.getInstance()
    val  docCart = "cart_android_test"
    fun updateCartData(qty: Int){
        val data: MutableMap<String, Any> = hashMapOf(
            "brandId" to "brandId",
            "categoryCode" to "categoryCode",
            "categoryMainCode" to "categoryMainCode",
            "categorySubCode" to "categorySubCode",
            "date" to Timestamp.now(),
            "image" to "itemImg",
            "name" to "itemName",
            "price" to "itemPrice",
            "productId" to "productId",
            "qty" to qty,
            "shipping" to "shipping"
        )

        val productlist: MutableMap<String, Any> = hashMapOf(
            "0" to data
        )

        val doc = db.collection(docCart).document(Userdata.uid.toString())
        doc.update("productlist", FieldValue.arrayUnion(productlist))
            .addOnSuccessListener {
                Log.d("update qty", qty.toString())
            }

    }

    fun getSetCartdata(qty: Int, position: Int){
        val doc = db.collection(docCart).document(Userdata.uid.toString())
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null) {
                    val map: MutableMap<*, *>? = documentSnapshot.data
                    for (entry in map!!.entries) {
                        val list = entry.value as ArrayList<Any>
//                        Log.d("getCartdata list",list.toString())
                        val newList = ArrayList<Any>()
                        for ((index, each) in list.withIndex()) {
                            val itemdata: MutableMap<String, String>? = each as MutableMap<String, String>?
                            if (itemdata != null) {

                                if (index == position){
                                    itemdata.set("qty", qty.toString())
                                }
                                newList.add(itemdata)
                            }
                        }
                        if (qty == 0){
                            newList.removeAt(position)
                        }
                        val productList = hashMapOf(
                            "productlist" to newList
                        )
                        db.collection(docCart).document(Userdata.uid.toString())
                            .set(productList)
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
                    val list: Any? = map.get("shipping")
                    if (list != null) {
//                        shipping = list
                    }
                }


            }
        return  task
    }
    private var taskShop: Task<DocumentSnapshot>? = null
    fun getShopData(userId: String): Task<DocumentSnapshot> {
         taskShop = db.collection("shops")
            .document(userId)
            .get()
        return taskShop as Task<DocumentSnapshot>
    }

}