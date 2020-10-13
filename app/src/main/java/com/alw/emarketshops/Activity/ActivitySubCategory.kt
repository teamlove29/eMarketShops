package com.alw.emarketshops.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterCategoryCard
import com.alw.emarketshops.Adapter.AdapterCategoryTopview
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Userdata.uid
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
        getLastSubCategory()
        toolbarSub.setOnClickListener {
            val intent = Intent(this, ActivityCategory::class.java)
            startActivity(intent)
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

    fun  getLastSubCategory(){
        val ref= FirebaseController.Firebase.db.collection("category_last_view").document(uid!!)
            ref.get().addOnCompleteListener{
                val newArray = ArrayList<ModelSubCategoryCard>()
                if (it.result?.get("category_sub") !== null){
                    val list = it.result?.get("category_sub") as ArrayList<*>
                    for (doc in  list){
                        val data: MutableMap<*, *>? = doc as MutableMap<*, *>?
                        val nameTH = data?.get("nameTH")?.toString()
                        val code = data?.get("cateCode")?.toString()
                        val mainCateCode = data?.get("mainCateCode")?.toString()
                        val uri:Uri = Uri.parse(data?.get("src")?.toString())
                        newArray.add((ModelSubCategoryCard(code!!,mainCateCode!!,nameTH!!,uri)))
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