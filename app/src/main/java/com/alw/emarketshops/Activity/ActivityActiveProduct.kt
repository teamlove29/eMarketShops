package com.alw.emarketshops.Activity

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterItemCard
import com.alw.emarketshops.Adapter.AdapterProductCard
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.Model.ModelItemCard
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_active_product.*


class ActivityActiveProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_product)
        getProductlist(2)
//        setSupportActionBar(toolbarProductAct)
        toolbarProductAct.overflowIcon = ContextCompat.getDrawable(this, R.drawable.baseline_more_vert_white_24dp)
        toolbarProductAct.setOnClickListener {
            finish()
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_bar, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title == "Grid"){
            getProductlist(2)
        }else{
            getProductlist(1)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductlist(column:Int){
        db.collection("product")
            .whereEqualTo("isActive", true)
            .whereEqualTo("isReady", true)
//            .whereEqualTo("categoryMainCode", mainCateCode)
//            .whereEqualTo("categorySubCode", subCateCode)
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
                                uri = Uri.parse(imgdata["imgUrl"].toString())
                            }
                        }
                       if (stock.toLong() > 0) {
                           newArrayList.add(
                               (ModelItemCard(
                                   id,
                                   uri,
                                   name,
                                   price,
                                   stock,
                                   detail,
                                   brand
                               ))
                           )
                       }
                    }


                }
                val adapter = AdapterItemCard(newArrayList, this)
                val adapterList = AdapterProductCard(newArrayList,this)
                recyclerViewProductAct.layoutManager =
                    GridLayoutManager(this, column, GridLayoutManager.VERTICAL, false)
                if (column == 1){
                    recyclerViewProductAct.adapter = adapterList
                }else {
                    recyclerViewProductAct.adapter = adapter
                }
            }
    }
}