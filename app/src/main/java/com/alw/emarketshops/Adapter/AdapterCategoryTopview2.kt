package com.alw.emarketshops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivitySubCategory
import com.alw.emarketshops.Activity.ActivitySubCategory2
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_category_card_topview.view.*

class AdapterCategoryTopview2(val arrayList: ArrayList<ModelSubCategoryCard>, val context: ActivitySubCategory2):
    RecyclerView.Adapter<AdapterCategoryTopview2.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mainCatecode:String=""
        var cateCode:String=""
        var cateName:String=""
        fun  bindCates(modelCategory: ModelSubCategoryCard){
            if (modelCategory.img.toString() !== "") {
                Picasso.get().load(modelCategory.img)
//                    .resize(80,80)
                    .into(itemView.imgSubCateCardTopview)
            }
            itemView.textViewSubCateNameTopview.text  = modelCategory.nameTH
            mainCatecode = modelCategory.mainCateCode
            cateCode = modelCategory.cateCode
            cateName = modelCategory.nameTH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_category_card_topview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {
            context.getSubCategory2(holder.cateCode,holder.mainCatecode)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}