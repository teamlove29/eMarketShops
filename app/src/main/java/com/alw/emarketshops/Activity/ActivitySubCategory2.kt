package com.alw.emarketshops.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard
import com.alw.emarketshops.Adapter.AdapterSubcategoryCard2
import com.alw.emarketshops.Model.ModelCategoryCard
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

        toolbarSub2.setOnClickListener {
            this.finish()
        }
    }

    private fun getSubCategory2(cateCode:String,mainCatecode:String){
        db.collection("category").document(cateCode)
            .get().addOnSuccessListener {
                val newArrayList = ArrayList<ModelCategoryCard>()
                val list = it.data?.get("subs") as ArrayList<Any>
                for (dt in list){
                    val catedata: MutableMap<*, *>? = dt as MutableMap<*, *>?
                    if (catedata?.get("mainCateCode")?.toString()  == mainCatecode) {
                        val list_subs = catedata?.get("subs") as ArrayList<Any>
                        for (sl in list_subs) {
                            val subcatedata: MutableMap<*, *>? = sl as MutableMap<*, *>?
                            val nameTH : String = subcatedata?.get("nameTH").toString()
                            val uri: Uri = Uri.parse(subcatedata?.get("src").toString())
                            val mainCateCode:String = subcatedata?.get("mainCateCode").toString()
                            val subCateCode:String = subcatedata?.get("subCateCode").toString()
                            newArrayList.add(ModelCategoryCard(subCateCode,mainCateCode,nameTH,uri))
                        }
                    }
                }

                val  adapterCategoryCard = AdapterSubcategoryCard2(newArrayList,this)
                recyclerViewSubCate2.layoutManager  = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                recyclerViewSubCate2.adapter = adapterCategoryCard
            }
    }
}