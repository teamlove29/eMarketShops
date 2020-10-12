package com.alw.emarketshops.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterCategoryCard
import com.alw.emarketshops.Adapter.AdapterCategoryTopview
import com.alw.emarketshops.Adapter.AdapterLastCategoryTopview
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category.*

class ActivityCategory : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    val newArray = ArrayList<ModelCategoryCard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        getCategory()
        lastgetCategory()
        toolbar.setOnClickListener {
            this.finish()
        }
    }

    fun  getCategory(){
        db.collection("category")
            .get().addOnCompleteListener{
                if (it.isSuccessful){
                    for (doc in it.result!!){
                        val nameTH = doc["nameTH"].toString()
                        val code = doc["cateCode"].toString()
                        val uri:Uri = Uri.parse(doc["src"].toString())
                        newArray.add((ModelCategoryCard(code,"",nameTH,uri)))
                    }

                    val  adapterCategoryCard = AdapterCategoryCard(newArray,this)
                    recyclerViewCate.layoutManager  = GridLayoutManager(this,
                        1,
                        GridLayoutManager.VERTICAL,
                        false)
                    recyclerViewCate.adapter = adapterCategoryCard
                }
            }
    }
    fun getSubCategory(code:String,cateName:String){
        val i = Intent(this, ActivitySubCategory::class.java)
        i.putExtra("code",code)
        i.putExtra("cateName",cateName)
        startActivity(i)

    }

    fun  lastgetCategory() {

        if (uid == null) {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        } else {

            db.collection("category_last_view")
                .document(uid!!)
                .get().addOnCompleteListener {
                    val newArray = ArrayList<ModelCategoryCard>()
                    if (it.result?.get("category") !== null) {

                        val list = it.result?.get("category") as ArrayList<*>
                        for (each in list) {
                            val data: MutableMap<*, *>? = each as MutableMap<*, *>?
                            val nameTH = data?.get("nameTH")?.toString()
                            val code = data?.get("cateCode")?.toString()
                            val uri: Uri = Uri.parse(data?.get("src")?.toString())
                            newArray.add((ModelCategoryCard(code!!, "", nameTH!!, uri)))
                        }
                        val adapterCategoryCard = AdapterLastCategoryTopview(newArray, this)
                        recyclerViewCate_top.layoutManager = GridLayoutManager(
                            this,
                            1,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )
                        recyclerViewCate_top.adapter = adapterCategoryCard
                        recyclerViewCate_top.smoothScrollToPosition(adapterCategoryCard.itemCount)
                    }
                }

        }
    }

}