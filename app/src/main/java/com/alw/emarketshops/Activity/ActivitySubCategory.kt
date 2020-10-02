package com.alw.emarketshops.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
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

        toolbarSub.setOnClickListener {
            this.finish()
        }
    }

    private fun getSubCategory(code:String){
        db.collection("category").whereEqualTo("cateCode", code)
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<ModelCategoryCard>()
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
                                            newArrayList.add(ModelCategoryCard(code,mainCateCode,nameTH,uri))
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
}