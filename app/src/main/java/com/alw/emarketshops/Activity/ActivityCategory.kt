package com.alw.emarketshops.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterCategoryCard
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category.*

class ActivityCategory : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    val newArray = ArrayList<ModelCategoryCard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        getCategory()
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
                        val uri:Uri = Uri.parse("")
                        newArray.add((ModelCategoryCard(code,"",nameTH,uri)))
                    }

                    val  adapterCategoryCard = AdapterCategoryCard(newArray,this)
                    recyclerViewCate.layoutManager  = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
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

}