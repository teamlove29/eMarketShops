package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityProducts
import com.alw.emarketshops.Activity.ActivitySubCategory2
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_card.view.*

class AdapterSubcategoryCard2(val arrayList: ArrayList<ModelCategoryCard>, val context: Context):
    RecyclerView.Adapter<AdapterSubcategoryCard2.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var subCateCode:String=""
        var mainCateCode:String=""
        var cateName:String=""
        fun  bindCates(modelCategory: ModelCategoryCard){
            Picasso.get().load(modelCategory.img).into(itemView.imgCateCard)
            println(modelCategory.img.toString())
            if (modelCategory.img.toString() == ""){

                Picasso.get().load(modelCategory.img).into(itemView.imgCateCard)
            }else{
                itemView.imgCateCard.setImageResource(R.drawable.e_market_shops_bw)
            }
            itemView.textViewCateName.text  = modelCategory.nameTH
            mainCateCode = modelCategory.mainCateCode
            subCateCode = modelCategory.cateCode
            cateName = modelCategory.nameTH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_card,parent,false)
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

        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}