package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ItemDetailActivity
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelMyOrderList
import com.alw.emarketshops.R
import com.google.firebase.firestore.DocumentSnapshot
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_my_order.view.*
import kotlinx.android.synthetic.main.item_card.view.*
import org.w3c.dom.Document


class AdapterMyOrderList(val arrayList: ArrayList<ModelMyOrderList>, val context: Context):
    RecyclerView.Adapter<AdapterMyOrderList.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var productId:String = ""
        var imgUrl:String=""
        var dtItem: DocumentSnapshot? = null
        fun bindMyOrderList(modelMyOrderList: ModelMyOrderList){
            itemView.txt_orderProductQty.text = " x ${modelMyOrderList.qty}"
            val total = modelMyOrderList.qty.toDouble() * modelMyOrderList.price.toDouble()
            itemView.txt_orderTotal.text ="Total : ${total.toString()}"
            itemView.btnShipping.text = modelMyOrderList.shipping
            productId = modelMyOrderList.productId
            val task = FirebaseController().getShopData(modelMyOrderList.sellerId)
            task?.addOnSuccessListener {
                itemView.txt_orderShopName.text = it["shopName"].toString()
            }
            val dtProduct = FirebaseController().getProductData(modelMyOrderList.productId)
            dtProduct?.addOnSuccessListener {
                dtItem = it
                itemView.txt_orderProductName.text = it["name"].toString()
                var uri = Uri.parse("")
                val dt:Any? = it["images"]
                val ls = dt as ArrayList<Any>
                for ((index, each) in ls.withIndex()){
                    val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                    if (imgdata !== null && index == 0){
                        imgUrl = imgdata["imgUrl"].toString()
                        uri = Uri.parse(imgUrl)
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
        holder.itemView.imageProduct.setOnClickListener {
            println(holder.dtItem?.get("name"))

            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("itemImg",holder.imgUrl)
            intent.putExtra("itemName",holder.dtItem?.get("name").toString())
            intent.putExtra("itemPrice",holder.dtItem?.get("price").toString())
            intent.putExtra("itemStock",holder.dtItem?.get("stock").toString())
            intent.putExtra("itemDetail",holder.dtItem?.get("detail").toString())
            intent.putExtra("itemBrand",holder.dtItem?.get("brand").toString())
            intent.putExtra("id",holder.productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
}