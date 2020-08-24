package com.alw.emarketshops

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.ui.cart.CartFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_item_list.view.*
import kotlinx.android.synthetic.main.fragment_cart.view.*

class AdapterItemList(val arrayList: ArrayList<ModelItemCartList>, val context: CartFragment):RecyclerView.Adapter<AdapterItemList.ViewHolder>() {

    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        var qty=0
        private val firebaseController = FirebaseController()
        @SuppressLint("SetTextI18n")
        fun  bindItemList(modelItemCard: ModelItemCartList){
            Picasso.get().load(modelItemCard.uri).resize(100, 100).into(itemView.imageItemList)
            itemView.textViewName.text = modelItemCard.itemName
            itemView.textViewPrice.text = (modelItemCard.itemPrice.toLong() * modelItemCard.itemQty.toLong()).toString() +" ฿"
            itemView.textViewQty.text = modelItemCard.itemQty + " ชิ้น"

            itemView.btnCartQtyUp.setOnClickListener {
                qty =Integer.parseInt(itemView.textViewQty.text.toString().substringBefore(" ชิ้น"))
                qty+=1
                itemView.textViewQty.text = (qty).toString() + " ชิ้น"
                val position = adapterPosition
                firebaseController.getSetCartdata(qty,position)

            }
            itemView.btnCartQtydown.setOnClickListener {
                 qty =Integer.parseInt(
                     itemView.textViewQty.text.toString().substringBefore(" ชิ้น")
                 )
                qty-=1
                itemView.textViewQty.text = (qty).toString() + " ชิ้น"

                val position = adapterPosition
                firebaseController.getSetCartdata(qty,position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_item_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItemList(arrayList[position])
    }


}