package com.alw.emarketshops.Activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterProductCard
import com.alw.emarketshops.Model.ModelItemCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_products.*

class ActivityProducts : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val i = intent
        toolbarProduct.title = i.getStringExtra("cateName")
        getProductlist(i.getStringExtra("mainCateCode"),i.getStringExtra("subCateCode"))

        toolbarProduct.setOnClickListener {
            this.finish()
        }
    }
    fun getProductlist(mainCateCode:String,subCateCode:String){
        Log.d("code >>",mainCateCode)
        db.collection("product")
            .whereEqualTo("isActive", true).whereEqualTo("isReady", true)
            .whereEqualTo("categoryMainCode", mainCateCode)
            .whereEqualTo("categorySubCode", subCateCode)
            .get()
            .addOnCompleteListener{
                val newArrayList = ArrayList<ModelItemCard>()
                if (it.isSuccessful){
                    for (doc in it.result!!){
                        val name : String = doc["name"].toString()
                        val price :String = doc["price"].toString()
                        val id:String = doc.id
                        val detail:String = doc["detail"].toString()
                        val brand:String = doc["brand"].toString()
                        val stock:String  = doc["stock"].toString()
                        var uri: Uri = Uri.parse("")
                        val dt:Any? = doc["images"]
                        val list = dt as ArrayList<Any>
                        for ((index, each) in list.withIndex()){
                            val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                            if (imgdata !== null && index == 0){
                                 uri = Uri.parse(imgdata.get("imgUrl").toString())
                            }
                        }
                        newArrayList.add((ModelItemCard(id, uri, name, price, stock, detail, brand)))
                    }


                }
                val adapter = AdapterProductCard(newArrayList,this)
                recyclerViewProduct.layoutManager =
                    GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                recyclerViewProduct.adapter = adapter
            }
    }
}