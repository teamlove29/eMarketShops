package com.alw.emarketshops.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.*
import com.alw.emarketshops.R
import com.alw.emarketshops.ui.ModelUser
import com.alw.emarketshops.ui.home.HomeFragment
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat


class CartFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var arrayList = ArrayList<ModelItemCartList>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getCartdata()

        btnCart_pay.setOnClickListener {
            val intent = Intent (activity, ActivitySelectPayment::class.java)
            intent.putExtra("total",textTotalCart.text)
            startActivity(intent)
        }

    }

    fun getCartdata(){
//        HomeFragment.Userdata.uid
//        Log.d("Userdata uid",HomeFragment.Userdata.uid)
            val doc = db.collection("cart_android_test").document(HomeFragment.Userdata.uid.toString())
            doc.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    if (documentSnapshot.data !== null) {
                        var totalCart: Long = 0
                        val newArrayList = ArrayList<ModelItemCartList>()
                        val map: MutableMap<*, *>? = documentSnapshot.data
                        for (entry in map!!.entries) {
                            val list = entry.value as ArrayList<Any>
                            for (each in list) {
                                val itemdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                if (itemdata != null) {

                                    val name: String = itemdata["name"].toString()
                                    val price: String = itemdata["price"].toString()
                                    val uri = Uri.parse(itemdata["image"].toString())
                                    var qty: String = itemdata["qty"].toString()
                                    totalCart += (price.toLong() * qty.toLong())
                                    newArrayList.add(ModelItemCartList(name, price, qty, uri))
                                }

                            }
                            arrayList = newArrayList
                            val adapterItemCard = AdapterItemList(arrayList, this)
                            itemList.layoutManager =
                                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                            itemList.adapter = adapterItemCard
                            textTotalCart.text = totalCart.toString()
                            val dec = DecimalFormat("#,###.00")
                            textTotalCart.text = dec.format(totalCart)
                        }
                    } else {
                        Toast.makeText(activity, "no cart data", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

}