package com.alw.emarketshops.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Model.ModelSubCategoryCard
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_category_card_topview.view.*

class AdapterCategoryTopview(val arrayList: ArrayList<ModelSubCategoryCard>, val context: Context):
    RecyclerView.Adapter<AdapterCategoryTopview.ViewHolder>() {
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
//
//            val i = Intent(context, ActivitySubCategory2::class.java)
//            i.putExtra("cateName",holder.cateName)
//            i.putExtra("mainCatecode",holder.mainCatecode)
//            i.putExtra("cateCode",holder.cateCode)
//            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}