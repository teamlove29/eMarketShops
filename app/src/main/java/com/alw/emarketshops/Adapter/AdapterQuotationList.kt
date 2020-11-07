package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityQuotation
import com.alw.emarketshops.Activity.ActivitySubCategory
import com.alw.emarketshops.Model.ModelQuotationList
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.card_quo_list.view.*

class AdapterQuotationList(val arrayList: ArrayList<ModelQuotationList>, val context:Context):
    RecyclerView.Adapter<AdapterQuotationList.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var RefNo =""
        fun bindItemList(modelQuotationList: ModelQuotationList){
            itemView.txtQuoNo.text ="Ref No. " +  modelQuotationList.id
            itemView.txtQuoCate.text = modelQuotationList.category
            itemView.txtQuoProduct.text = modelQuotationList.product
            itemView.txtQuoDate.text = modelQuotationList.date
            itemView.txtDes.text = modelQuotationList.description
            RefNo = modelQuotationList.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_quo_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItemList(arrayList[position])
        holder.itemView.setOnClickListener {
            val i = Intent(context, ActivityQuotation::class.java)
            i.putExtra("quotationNo",holder.RefNo)
            context.startActivity(i)

        }
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }
}