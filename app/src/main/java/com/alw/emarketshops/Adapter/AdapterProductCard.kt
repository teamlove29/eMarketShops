package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ItemDetailActivity
import com.alw.emarketshops.Model.ModelItemCard
import com.alw.emarketshops.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_card_landscape.view.*
import kotlinx.android.synthetic.main.item_card_landscape.view.imgItemCardLans

class AdapterProductCard(val arrayList: ArrayList<ModelItemCard>, val context: Context) :
    RecyclerView.Adapter<AdapterProductCard.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(modelItem: ModelItemCard) {
            Picasso.get().load(modelItem.uri)
//                .resize(100,100)
                .into(itemView.imgItemCard)
            itemView.textViewName.text = modelItem.itemName
            itemView.textViewPrice.text = modelItem.itemPrice+" ฿"
            itemView.textViewStock.text = modelItem.itemStock + " ชิ้น"

            itemView.setOnClickListener { v: View ->

                val context = v.context
                val intent = Intent(context, ItemDetailActivity::class.java)
                intent.putExtra("itemImg",modelItem.uri.toString())
                intent.putExtra("itemName",modelItem.itemName)
                intent.putExtra("itemPrice",modelItem.itemPrice)
                intent.putExtra("itemStock",modelItem.itemStock)
                intent.putExtra("itemDetail",modelItem.Detail)
                intent.putExtra("itemBrand",modelItem.Brand)
                intent.putExtra("id",modelItem.id)
                context.startActivity(intent)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }
}