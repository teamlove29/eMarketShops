package com.alw.emarketshops.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Fragment.CartFragment
import com.alw.emarketshops.Model.ModelItemCartList
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_item_list.view.*
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.DecimalFormat

class AdapterItemList(val arrayList: ArrayList<ModelItemCartList>, val context: CartFragment):RecyclerView.Adapter<AdapterItemList.ViewHolder>() {

    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        var btn1:Button = itemView.btnCartQtyUp
        var btn2:Button = itemView.btnCartQtydown
        var cbSelect = itemView.cbSelect
        var txtqty = itemView.textViewQty
        var txtprice = itemView.textViewPrice
        var itemprice =0.00
        @SuppressLint("SetTextI18n")
        fun  bindItemList(modelItemCard: ModelItemCartList){
            Picasso.get().load(modelItemCard.uri)
//                .resize(50, 50)
                .into(itemView.imageItemList)
            itemprice = modelItemCard.itemPrice.toDouble()
            itemView.textViewName.text = modelItemCard.itemName
            itemView.textViewPrice.text = (modelItemCard.itemPrice.toDouble() * modelItemCard.itemQty.toDouble()).toString() +" ฿"
            itemView.textViewQty.text = modelItemCard.itemQty + " ชิ้น"
            itemView.cbSelect.isChecked = modelItemCard.check
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
        val firebaseController = FirebaseController()

        holder.bindItemList(arrayList[position])
        holder.btn1.setOnClickListener {
            var qty =Integer.parseInt(holder.txtqty.text.toString().substringBefore(" ชิ้น"))
            qty+=1
            holder.txtqty.text = (qty).toString() + " ชิ้น"
            holder.txtprice.text = (holder.itemprice * qty.toLong()).toString()+" ฿"
            firebaseController.getSetCartdata(qty,position,context)
        }
        holder.btn2.setOnClickListener {
            var qty =Integer.parseInt(holder.txtqty.text.toString().substringBefore(" ชิ้น"))
            qty-=1
            firebaseController.getSetCartdata(qty,position,context)

            if (qty == 0){
                Log.d("arrayList removeAt =>>",position.toString())
                println(arrayList[position].itemName)
                arrayList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                notifyItemRangeChanged(holder.adapterPosition,arrayList.size)

            }else {
                holder.txtqty.text = (qty).toString() + " ชิ้น"
                holder.txtprice.text = (holder.itemprice * qty.toLong()).toString()+" ฿"
            }


        }
        holder.cbSelect.setOnClickListener {
            println(holder.cbSelect.hasSelection())
            var state = false
            if (holder.cbSelect.isChecked){state = true}
            firebaseController.getSetSelect(state,position,context)
        }
    }


}