package com.alw.emarketshops

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_card.view.*

class AdapterCategoryCard(val arrayList: ArrayList<ModelCategoryCard>, val context: ActivityCategory):RecyclerView.Adapter<AdapterCategoryCard.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var code:String=""
        var cateName:String=""
        fun  bindCates(modelCategory: ModelCategoryCard){
//            Picasso.get().load(modelCategory.img).into(itemView.imgCateCard)
            itemView.imgCateCard.setImageResource(R.drawable.e_market_shops_bw)
            itemView.textViewCateName.text  = modelCategory.nameTH
            code = modelCategory.code
            cateName = modelCategory.nameTH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {
//          context.getSubCategory(holder.code,holder.cateName)
            val i = Intent(context, ActivitySubCategory::class.java)
            i.putExtra("code",holder.code)
            i.putExtra("cateName",holder.cateName)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}