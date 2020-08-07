package com.alw.emarketshops

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.ui.home.HomeFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_card.view.*
import com.squareup.picasso.Picasso

class AdapterItemCard(val arrayList: ArrayList<ModelItemCard>, val context: HomeFragment) :RecyclerView.Adapter<AdapterItemCard.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(modelItem: ModelItemCard) {
//            itemView.imgItemCard.setImageResource(modelItem.img)
            Picasso.get().load(modelItem.uri).resize(100,100).into(itemView.imgItemCard)
            itemView.textViewName.text = modelItem.itemName
            itemView.textViewPrice.text = modelItem.itemPrice+" ฿"
            itemView.textViewStock.text = modelItem.itemStock + " ชิ้น"

            itemView.setOnClickListener { v: View  ->

//                var position: Int = getAdapterPosition()
//                Snackbar.make(v, "Click detected on item $position",
//                Snackbar.LENGTH_LONG).setAction("Action", null).show()
                Log.d("product id", modelItem.id)

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
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

    }
}



