package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.*
import com.alw.emarketshops.Activity.ActivitySelectPayment
import com.alw.emarketshops.Adapter.AdapterItemList
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.Model.ModelItemCartList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat


class CartFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var arrayList = ArrayList<ModelItemCartList>()
    private val firebaseController = FirebaseController()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        btnCart_pay.setOnClickListener {
            val intent = Intent (activity, ActivitySelectPayment::class.java)
            intent.putExtra("total",textsubTotalCart.text)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        getCartdata()
    }

    fun getCartdata(){

      println("CartFrag getCartdata")
      var totalCart: Long = 0
       db.collection("cart")
          .document(FirebaseController.Userdata.uid.toString())
          .get().addOnSuccessListener { documentSnapshot ->

          if (documentSnapshot != null) {
              if (documentSnapshot.data !== null) {
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
                                    val qty: String = itemdata["qty"].toString()
                                    totalCart += (price.toLong() * qty.toLong())
                                    newArrayList.add(ModelItemCartList(name, price, qty, uri))
                                }

                            }
//                            arrayList = newArrayList
                            val adapterItemCard = AdapterItemList(newArrayList, this)
                            itemList.layoutManager =
                                GridLayoutManager(this.context, 1,
                                    GridLayoutManager.VERTICAL, false)
                            itemList.adapter = adapterItemCard
                            val dec = DecimalFormat("#,###.00")
                            textsubTotalCart.text = dec.format(totalCart)
                        }
                    } else {
                        Toast.makeText(activity, "no cart data", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }


}