package com.alw.emarketshops.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterCategoryTopview
import com.alw.emarketshops.Adapter.AdapterCategoryTopview2
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard2
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.activity_sub_category.recyclerViewSubCate
import kotlinx.android.synthetic.main.activity_sub_category2.*

class ActivitySubCategory2 : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category2)

        val i = intent
        val mainCatecode = i.getStringExtra("mainCatecode").toString()
        val cateCode = i.getStringExtra("cateCode").toString()
        toolbarSub2.title =i.getStringExtra("cateName")

        getSubCategory2(cateCode,mainCatecode)
        //getSubCategory(cateCode)
        getLastSubCategory2()

        toolbarSub2.setOnClickListener {

            this.finish()
        }
    }

    fun getSubCategory2(cateCode:String,mainCatecode:String){
        println(cateCode)
        db.collection("category").document(cateCode)
            .get().addOnSuccessListener {
                val newArrayList = ArrayList<ModelSubCategoryCard>()
                val list = it.data?.get("subs") as ArrayList<Any>
                for (dt in list){
                    val catedata: MutableMap<*, *>? = dt as MutableMap<*, *>?
                    if (catedata?.get("mainCateCode")?.toString()  == mainCatecode) {
                        val list_subs = catedata["subs"] as ArrayList<Any>
                        for (sl in list_subs) {
                            val subcatedata: MutableMap<*, *>? = sl as MutableMap<*, *>?
                            val nameTH : String = subcatedata?.get("nameTH").toString()
                            val uri: Uri = Uri.parse(subcatedata?.get("src").toString())
                            val mainCateCode:String = subcatedata?.get("mainCateCode").toString()
                            val subCateCode:String = subcatedata?.get("subCateCode").toString()
                            newArrayList.add(ModelSubCategoryCard(subCateCode,mainCateCode,nameTH,uri))
                        }
                    }
                }

                val  adapterCategoryCard = AdapterSubcategoryCard2(newArrayList,this)
                recyclerViewSubCate2.layoutManager  = GridLayoutManager(this,
                    1, GridLayoutManager.VERTICAL, false)
                recyclerViewSubCate2.adapter = adapterCategoryCard
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


                                val  adapterCategoryCard = AdapterCategoryTopview2(newArrayList,this)
//                                recyclerViewCate_top2.layoutManager  = GridLayoutManager(this,
//                                    1, GridLayoutManager.HORIZONTAL, false)
//                                recyclerViewCate_top2.adapter = adapterCategoryCard
                            }

                    }

                }
            }
    }

    fun  getLastSubCategory2(){
        val ref= FirebaseController.Firebase.db.collection("category_last_view").document(
            FirebaseController.Userdata.uid!!)
        ref.get().addOnCompleteListener{
            val newArray = ArrayList<ModelSubCategoryCard>()
            if (it.result?.get("category_sub2") !== null){
                val list = it.result?.get("category_sub2") as ArrayList<*>
                for (doc in  list){
                    val data: MutableMap<*, *>? = doc as MutableMap<*, *>?
                    val nameTH = data?.get("nameTH")?.toString()
                    val code = data?.get("cateCode")?.toString()
                    val mainCateCode = data?.get("mainCateCode")?.toString()
                    val uri:Uri = Uri.parse(data?.get("src")?.toString())
                    newArray.add((ModelSubCategoryCard(code!!,mainCateCode!!,nameTH!!,uri)))
                }

                val  adapterCategoryCard = AdapterCategoryTopview2(newArray,this)
//                recyclerViewCate_top2.layoutManager  = GridLayoutManager(this,
//                    1,
//                    GridLayoutManager.HORIZONTAL,
//                    false)
//                recyclerViewCate_top2.adapter = adapterCategoryCard
            }
        }
    }
}