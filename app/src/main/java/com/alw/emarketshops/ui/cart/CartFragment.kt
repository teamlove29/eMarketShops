package com.alw.emarketshops.ui.cart

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.ActivitySelectPayment
import com.alw.emarketshops.AdapterItemList
import com.alw.emarketshops.ModelItemCartList
import com.alw.emarketshops.R
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private var arrayList = ArrayList<ModelItemCartList>()
    private val colRefCart = db.collection("cart")
    private var mListener: ListenerRegistration? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getList()
//        addMultiDocsListener()

        btnCart_pay.setOnClickListener {
            val intent = Intent (activity, ActivitySelectPayment::class.java)
            startActivity(intent)
        }

    }

    private fun getList() {

        db.collection("cart")
            //.whereEqualTo("isActive", true).whereEqualTo("isReady", true)
            .get()
            .addOnCompleteListener { task ->
                val newArrayList = ArrayList<ModelItemCartList>()
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val product = document["productList"] as List<Map<String, Any>>?
//
//                        Log.d("TAG", "getList: " + product?.get(0).toString())
//                        val detailProduct = product?.get(0).toString()
//                            .replace("{","[")
//                            .replace("}","]")
////                            .split(",")
//                        Log.d("TAG", "detailProduct : " + detailProduct.get(0))
//                        Log.d(ContentValues.TAG, "productlist : " + product?.get(0))
//                        Log.d(ContentValues.TAG, "rebuildArray : " + rebuildArray(product?.get(0).toString()))

                        val name: String = "name test"
                        val price: String = "100"

                        val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/emarketshops-c948d.appspot.com/o/product%2Fimage2_800x800.jpg?alt=media&token=491b9c75-8e75-41e2-92d9-2753b6fb5aaa")
                        val id: String = "document.id"
                        val detail: String = "detail"
                        val brand: String = "brand"
                        var qty: String = "1"

                        newArrayList.add(ModelItemCartList(name,price,qty,uri))
                        newArrayList.add(ModelItemCartList(name,price,qty,uri))
                    }

                }
                arrayList = newArrayList
//                Log.d(ContentValues.TAG, "arrayList.count : " + arrayList.count().toString())
                val adapterItemCard = AdapterItemList(arrayList, this)
                itemList.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                itemList.adapter = adapterItemCard
            }
    }
//    fun rebuildArray(list:String): List<Any> {
//        val nList = list
//            .replace("{","[")
//            .replace("}","]")
//        val strs: List<Any> = nList
//        return strs
//    }

    private fun addMultiDocsListener(){
        val mQuery: Query
        mQuery = colRefCart
        mListener = mQuery.addSnapshotListener { querySnapshot: QuerySnapshot?,
                                                 _: FirebaseFirestoreException? ->
            if (querySnapshot != null) {
                if (querySnapshot.getDocuments().size > 0){
                    for (document:DocumentSnapshot in querySnapshot){
//                        val itemCartList : ModelItemCartList? = document.toObject(ModelItemCartList::class.java)
                        Log.d("addMultiDocsListener ", document.data.toString())
                    }
                }

            }
        }

    }
}