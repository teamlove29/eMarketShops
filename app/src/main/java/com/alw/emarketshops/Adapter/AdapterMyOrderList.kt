package com.alw.emarketshops.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelMyOrderList
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_my_order.view.*
import kotlinx.android.synthetic.main.item_card.view.*

class AdapterMyOrderList(val arrayList: ArrayList<ModelMyOrderList>):
    RecyclerView.Adapter<AdapterMyOrderList.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindMyOrderList(modelMyOrderList: ModelMyOrderList){
            itemView.txt_orderProductQty.text = " x ${modelMyOrderList.qty}"
            val total = modelMyOrderList.qty.toDouble() * modelMyOrderList.price.toDouble()
            itemView.txt_orderTotal.text ="Total : ${total.toString()}"
            itemView.btnShipping.text = modelMyOrderList.shipping
            val task = FirebaseController().getShopData(modelMyOrderList.sellerId)
            task?.addOnSuccessListener {
                itemView.txt_orderShopName.text = it["shopName"].toString()
            }
            val dtProduct = FirebaseController().getProductData(modelMyOrderList.productId)
            dtProduct?.addOnSuccessListener {
                itemView.txt_orderProductName.text = it["name"].toString()
                var uri = Uri.parse("")
                val dt:Any? = it["images"]
                val ls = dt as ArrayList<Any>
                for ((index, each) in ls.withIndex()){
                    val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                    if (imgdata !== null && index == 0){
                        uri = Uri.parse(imgdata["imgUrl"].toString())
                    }
                }
                Picasso.get().load(uri).into(itemView.imageProduct)

            }
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