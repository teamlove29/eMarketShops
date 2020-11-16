package com.alw.emarketshops.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivitySubCategory
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FieldValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_category_card_topview.view.*

class AdapterCategoryTopview(val arrayList: ArrayList<ModelSubCategoryCard>, val context: ActivitySubCategory):
    RecyclerView.Adapter<AdapterCategoryTopview.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mainCatecode:String=""
        var cateCode:String=""
        var cateName:String=""
        var uri: Uri? =null
        fun  bindCates(modelCategory: ModelSubCategoryCard){
            if (modelCategory.img.toString() !== "") {
                Picasso.get().load(modelCategory.img)
                    .into(itemView.imgSubCateCardTopview)
            }
            itemView.textViewSubCateNameTopview.text  = modelCategory.nameTH
            mainCatecode = modelCategory.mainCateCode
            cateCode = modelCategory.cateCode
            cateName = modelCategory.nameTH
            uri = modelCategory.img
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_card_topview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {
            context.getSubCategory(holder.cateCode)
            insertCategoryLastView(ModelSubCategoryCard(holder.cateCode,holder.mainCatecode,holder.cateName,holder.uri!!))
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun insertCategoryLastView(modelCategory: ModelSubCategoryCard){
        println(modelCategory.nameTH)

        val data = hashMapOf(
            "nameTH" to modelCategory.nameTH,
            "cateCode" to modelCategory.cateCode,
            "mainCateCode" to modelCategory.mainCateCode,
            "img" to modelCategory.img.toString()
        )
        val category = hashMapOf(
            "category_sub2" to listOf(data)
        )

        val ref= FirebaseController.Firebase.db.collection("category_last_view").document(
            FirebaseController.Userdata.uid!!)
        ref.get().addOnSuccessListener {
            if (it.data !== null){

                ref.update("category_sub2", FieldValue.arrayRemove(data))
                ref.update("category_sub2", FieldValue.arrayUnion(data))
            }else{
                ref.set(category)
            }

        }
    }
}