package com.alw.emarketshops.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterCategoryCard
import com.alw.emarketshops.Adapter.AdapterCategoryTopview
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_sub_category.*

class ActivitySubCategory : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        val i = intent
        val code = i.getStringExtra("code")
        toolbarSub.title =i.getStringExtra("cateName")
        getSubCategory(code)
        getCategory()
        toolbarSub.setOnClickListener {
            this.finish()
        }
    }

    fun getSubCategory(code:String){
        db.collection("category").whereEqualTo("cateCode", code)
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<ModelSubCategoryCard>()
                if (task.isSuccessful){
                    for (doc in task.result!!){
                        db.collection("category").document(doc["cateCode"].toString())
                            .get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot != null) {
                                    if (documentSnapshot.data !== null) {
                                        val list = documentSnapshot["subs"] as ArrayList<Any>
                                        for (each in list) {
                                            val catedata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                            val nameTH : String = catedata?.get("nameTH").toString()
                                            val uri: Uri = Uri.parse(catedata?.get("src").toString())
                                            val mainCateCode:String = catedata?.get("mainCateCode").toString()
                                            newArrayList.add(ModelSubCategoryCard(code,mainCateCode,nameTH,uri))
                                        }
                                    }
                                }


                                val  adapterCategoryCard = AdapterSubcategoryCard(newArrayList,this)
                                recyclerViewSubCate.layoutManager  = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                                recyclerViewSubCate.adapter = adapterCategoryCard
                            }

                    }

                }
            }
    }

    fun  getCategory(){
        db.collection("category")
            .get().addOnCompleteListener{
                val newArray = ArrayList<ModelSubCategoryCard>()
                if (it.isSuccessful){
                    for (doc in it.result!!){
                        val nameTH = doc["nameTH"].toString()
                        val code = doc["cateCode"].toString()
                        val uri:Uri = Uri.parse(doc["src"].toString())
                        newArray.add((ModelSubCategoryCard(code,"",nameTH,uri)))
                    }

                    val  adapterCategoryCard = AdapterCategoryTopview(newArray,this)
                    recyclerViewCate_top1.layoutManager  = GridLayoutManager(this,
                        1,
                        GridLayoutManager.HORIZONTAL,
                        false)
                    recyclerViewCate_top1.adapter = adapterCategoryCard
                }
            }
    }
}