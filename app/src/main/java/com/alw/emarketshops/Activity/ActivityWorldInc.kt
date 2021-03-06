package com.alw.emarketshops.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterProductCard
import com.alw.emarketshops.Model.ModelItemCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.activity_world_inc.*

class ActivityWorldInc : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_inc)
        getProductlist()
        toolbarWorldInc.setOnClickListener {
            this.finish()
        }
    }

    fun getProductlist(){
        db.collection("product")
            .whereEqualTo("isActive", true)
            .whereEqualTo("isReady", true)
            .whereEqualTo("categoryCode", "C005")
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
                recycleViewWorld.layoutManager =
                    GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                recycleViewWorld.adapter = adapter
            }
    }
}