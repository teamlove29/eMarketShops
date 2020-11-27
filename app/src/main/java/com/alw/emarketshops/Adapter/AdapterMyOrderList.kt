package com.alw.emarketshops.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Model.ModelMyOrderList
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.card_my_order.view.*

class AdapterMyOrderList(val arrayList: ArrayList<ModelMyOrderList>):
    RecyclerView.Adapter<AdapterMyOrderList.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindMyOrderList(modelMyOrderList: ModelMyOrderList){
            itemView.txt_orderShopName.text = modelMyOrderList.shopname
            itemView.txt_orderProductName.text = modelMyOrderList.product
            itemView.txt_orderProductQty.text = modelMyOrderList.qty
            itemView.txt_orderTotal.text = modelMyOrderList.price
            itemView.btnShipping.text = modelMyOrderList.shipping
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_my_order,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMyOrderList(arrayList[position])
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
}