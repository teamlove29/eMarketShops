package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityCategory
import com.alw.emarketshops.Activity.ActivityProducts
import com.alw.emarketshops.Activity.ActivitySubCategory2
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
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
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}