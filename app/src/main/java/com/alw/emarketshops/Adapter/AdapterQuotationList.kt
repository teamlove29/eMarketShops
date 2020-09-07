package com.alw.emarketshops.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Model.ModelQuotationList
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.card_quo_list.view.*

class AdapterQuotationList(val arrayList: ArrayList<ModelQuotationList>, val context:Context):
    RecyclerView.Adapter<AdapterQuotationList.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItemList(modelQuotationList: ModelQuotationList){
            itemView.txtQuoNo.text = modelQuotationList.id
            itemView.txtQuoCate.text = modelQuotationList.category
            itemView.txtQuoProduct.text = modelQuotationList.product
            itemView.txtQuoDate.text = modelQuotationList.date
            itemView.txtDes.text = modelQuotationList.description
            itemView.setOnClickListener {
                val  context:Context = it.context
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Show")
                dialogBuilder.setMessage("Quotation Document")
                dialogBuilder.setPositiveButton("OK") { _, _ ->
                }
                dialogBuilder.show()

            }
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
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }
}