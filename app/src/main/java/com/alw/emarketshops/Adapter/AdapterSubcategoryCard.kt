package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityCategory
import com.alw.emarketshops.Activity.ActivityProducts
import com.alw.emarketshops.Activity.ActivitySubCategory2
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FieldValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.category_card.view.*
import kotlinx.android.synthetic.main.sub_category_card.view.*

class AdapterSubcategoryCard(val arrayList: ArrayList<ModelSubCategoryCard>, val context: Context):
    RecyclerView.Adapter<AdapterSubcategoryCard.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var mainCatecode:String=""
        var cateCode:String=""
        var cateName:String=""
        var uri: Uri? = null
        fun  bindCates(modelCategory: ModelSubCategoryCard){
            if (modelCategory.img.toString() !== "") {
                Picasso.get().load(modelCategory.img)
//                    .resize(80,80)
                    .into(itemView.imgSubCateCard)
            }
            itemView.textViewSubCateName.text  = modelCategory.nameTH
            mainCatecode = modelCategory.mainCateCode
            cateCode = modelCategory.cateCode
            cateName = modelCategory.nameTH
            uri = modelCategory.img
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {

            val i = Intent(context, ActivitySubCategory2::class.java)
            i.putExtra("cateName",holder.cateName)
            i.putExtra("mainCatecode",holder.mainCatecode)
            i.putExtra("cateCode",holder.cateCode)
            context.startActivity(i)
            insertCategoryLastView(ModelSubCategoryCard(holder.cateCode,holder.mainCatecode,holder.cateName,holder.uri!!))
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun insertCategoryLastView(modelCategory: ModelSubCategoryCard){

        val data = hashMapOf(
            "nameTH" to modelCategory.nameTH,
            "cateCode" to modelCategory.cateCode,
            "mainCateCode" to modelCategory.mainCateCode,
            "src" to modelCategory.img.toString()
        )
        val category = hashMapOf(
            "category_sub" to listOf(data)
        )

        val ref= FirebaseController.Firebase.db.collection("category_last_view").document(
            FirebaseController.Userdata.uid!!)
        ref.get().addOnSuccessListener {
            if (it.data !== null){

                ref.update("category_sub", FieldValue.arrayRemove(data))
                ref.update("category_sub", FieldValue.arrayUnion(data))
            }else{
                ref.set(category)
            }

        }
    }
}