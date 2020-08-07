package com.alw.emarketshops

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.ui.cart.CartFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_item_list.view.*
import kotlinx.android.synthetic.main.card_item_list.view.textViewName

class AdapterItemList(val arrayList: ArrayList<ModelItemCartList>, val context: CartFragment):RecyclerView.Adapter<AdapterItemList.ViewHolder>() {
    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun  bindItemList(modelItemCard: ModelItemCartList){
            Picasso.get().load(modelItemCard.uri).resize(120,100).into(itemView.imageItemList)
            itemView.textViewName.text = modelItemCard.itemName
            itemView.textViewPrice.text = modelItemCard.itemPrice +" ฿"
            itemView.textViewQty.text = modelItemCard.itemQty + " ชิ้น"

            itemView.btnCartQtyUp.setOnClickListener {
                val qty =Integer.parseInt(itemView.textViewQty.text.toString().substringBefore(" ชิ้น"))
                itemView.textViewQty.text = (qty+1).toString() + " ชิ้น"
            }
            itemView.btnCartQtydown.setOnClickListener {
                val qty =Integer.parseInt(itemView.textViewQty.text.toString().substringBefore(" ชิ้น"))
                itemView.textViewQty.text = (qty-1).toString() + " ชิ้น"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItemList(arrayList[position])
    }

}