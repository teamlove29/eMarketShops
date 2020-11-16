package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityProducts
import com.alw.emarketshops.Activity.ActivitySubCategory2
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FieldValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_card.view.*
import kotlinx.android.synthetic.main.sub_category_card2.view.*

class AdapterSubcategoryCard2(val arrayList: ArrayList<ModelSubCategoryCard>, val context: Context):
    RecyclerView.Adapter<AdapterSubcategoryCard2.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var subCateCode:String=""
        var mainCateCode:String=""
        var cateName:String=""
        var uri: Uri? = null
        fun  bindCates(modelCategory: ModelSubCategoryCard){
            if (modelCategory.img.toString() !== "") {
                Picasso.get().load(modelCategory.img)
//                    .resize(60,60)
                    .into(itemView.imgSubCateCard2)
            }
            itemView.textViewSubCateName2.text  = modelCategory.nameTH
            mainCateCode = modelCategory.mainCateCode
            subCateCode = modelCategory.cateCode
            cateName = modelCategory.nameTH
            uri = modelCategory.img
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_card2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {

            val i = Intent(context, ActivityProducts::class.java)
            i.putExtra("cateName",holder.cateName)
            i.putExtra("mainCateCode",holder.mainCateCode)
            i.putExtra("subCateCode",holder.subCateCode)
            context.startActivity(i)
            insertCategoryLastView(ModelSubCategoryCard(holder.subCateCode,holder.mainCateCode,holder.cateName,holder.uri!!))

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