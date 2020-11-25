package com.alw.emarketshops

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterItemList
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.Fragment.CartFragment
import com.alw.emarketshops.Model.ModelItemCartList
import com.alw.emarketshops.Model.ModelUser
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat


class FirebaseController {
    object  Userdata {
        private val modelUser = ModelUser()
        var uid = modelUser.user?.uid
        var name = modelUser.user?.displayName
        var email = modelUser.user?.email
    }
    object Firebase {
        val db = FirebaseFirestore.getInstance()

    }
    private var taskShop: Task<DocumentSnapshot>? = null

    val  docCart = "cart"



    fun getSetCartdata(qty: Int, position: Int,context: CartFragment){

        println("getSetCartdata")
        var total:Long=0
        db.collection(docCart).document(Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null) {
                    val newList = ArrayList<Any>()
                    val map: MutableMap<*, *>? = documentSnapshot.data
                    for (entry in map!!.entries) {
                        val list = entry.value as ArrayList<Any>
                        for ((index, each) in list.withIndex()) {
                            val itemdata: MutableMap<String, String>? = each as MutableMap<String, String>?
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
                        context.totalCart = total
                        context.txtTotal.text = total.toString()
                        val ref1 = FirebaseFirestore.getInstance()
                        ref1.collection(docCart).document(Userdata.uid.toString())
                            .set(productList)
                            .addOnSuccessListener {
                            }

                    }

                } else {
                    Log.d("cart data", "no cart data")
                }
            }


        }

    }
    fun getSetSelect(check: Boolean, position: Int,context: CartFragment){
        var total:Long=0
        db.collection(docCart).document(Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        val newList = ArrayList<Any>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<Any>
                            for ((index, each) in list.withIndex()) {
                                val itemdata: MutableMap<String, Any>? = each as MutableMap<String,Any>?
                                if (itemdata != null) {
                                    if (index == position){
                                        itemdata["isSelect"] = check
                                    }
                                    val price: String = itemdata["price"].toString()
                                    val nqty: String = itemdata["qty"].toString()

                                    val itemCkeck:Boolean = itemdata["isSelect"] as Boolean
                                    if (itemCkeck) {
                                        total += (price.toLong() * nqty.toLong())
                                    }
                                    newList.add(itemdata)
                                }
                            }

                            val productList = hashMapOf(
                                "productlist" to newList
                            )
                            val dec = DecimalFormat("#,###.00")
                            context.textsubTotalCart.text = dec.format(total)
                            context.txtTotal.text = total.toString()
                            val ref1 = FirebaseFirestore.getInstance()
                            ref1.collection(docCart).document(Userdata.uid.toString())
                                .set(productList)
                                .addOnSuccessListener {
                                }

                        }

                    } else {
                        Log.d("cart data", "no cart data")
                    }
                }


            }

    }
    fun SetSelectAll(state:Boolean,context: CartFragment) {

        println("getSetSelect")
        var total:Long=0
        db.collection(docCart).document(Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        val newList = ArrayList<Any>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<Any>
                            for (each in list) {
                                val itemdata: MutableMap<String, Any>? = each as MutableMap<String,Any>?
                                if (itemdata != null) {

                                    itemdata["isSelect"] = state

                                    val price: String = itemdata["price"].toString()
                                    val nqty: String = itemdata["qty"].toString()

                                    if (state) {

                                        total += (price.toLong() * nqty.toLong())
                                    }

                                    newList.add(itemdata)
                                }
                            }

                            val productList = hashMapOf(
                                "productlist" to newList
                            )
                            val dec = DecimalFormat("#,###.00")
                            context.textsubTotalCart.text = dec.format(total)
                            context.txtTotal.text = total.toString()
                            val ref1 = FirebaseFirestore.getInstance()
                            ref1.collection(docCart).document(Userdata.uid.toString())
                                .set(productList)
                                .addOnSuccessListener {
                                    getCartdata(context)
                                }

                        }

                    } else {
                        Log.d("cart data", "no cart data")
                    }
                }


            }

    }

    fun getCartdata(context: CartFragment){
        var totalCart:Long = 0
        db.collection("cart")
            .document(Userdata.uid.toString())
            .get().addOnSuccessListener { documentSnapshot ->

                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        val newArrayList = ArrayList<ModelItemCartList>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        var checkCount = 0

                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<Any>
                            for (each in list) {
                                val itemdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                println("list ${list.size}")
                                if (itemdata != null) {
                                    val name: String = itemdata["name"].toString()
                                    val price: String = itemdata["price"].toString()
                                    val uri = Uri.parse(itemdata["image"].toString())
                                    val qty: String = itemdata["qty"].toString()
                                    var check = false
                                    if (itemdata["isSelect"] !== null){
                                        check = itemdata["isSelect"] as Boolean
                                    }
                                    if (check) {
                                        totalCart += (price.toLong() * qty.toLong())
                                        checkCount += 1
                                    }
                                    newArrayList.add(ModelItemCartList(name, price, qty, uri,check))
                                }

                            }
                            context.cbSelectAll.isChecked = checkCount == list.size
                            val adapterItemCard = AdapterItemList(newArrayList, context)
                            context.itemList.layoutManager =
                                GridLayoutManager(context.context, 1,
                                    GridLayoutManager.VERTICAL, false)
                            context.itemList.adapter = adapterItemCard
                            val dec = DecimalFormat("#,###.00")
                            context.txtTotal.text = totalCart.toString()
                            context.textsubTotalCart.text = dec.format(totalCart)
                        }
                        context.btnCart_pay.isEnabled = true
                    } else {

                        context.btnCart_pay.isEnabled = false
                    }
                }
            }

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