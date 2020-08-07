package com.alw.emarketshops.ui.cart

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*

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
        getList()

    }

    private fun getList() {

        db.collection("cart")
            //.whereEqualTo("isActive", true).whereEqualTo("isReady", true)
            .get()
            .addOnCompleteListener { task ->
                val newArrayList = ArrayList<ModelItemCartList>()
                if (task.isSuccessful) {
                    for (document in task.result!!) {

                        val name: String = "name test"
                        val price: String = "100"
                        val productlist = listOf(document["productList"])

                        Log.d(ContentValues.TAG, "productlist : " + productlist[0])
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
                Log.d(ContentValues.TAG, "arrayList.count : " + arrayList.count().toString())
                val adapterItemCard = AdapterItemList(arrayList, this)
                itemList.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                itemList.adapter = adapterItemCard
            }
    }
    fun rebuildUrl(list:List<Any>,index:Int): Uri {
        val nList = list[index].toString()
            .replace("[","")
            .replace("]","")
        val strs = nList.split(",").toTypedArray()
        return Uri.parse(strs[index].substringAfter("=").substringBefore("}"))
    }
}