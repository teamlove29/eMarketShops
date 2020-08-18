package com.alw.emarketshops

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.ui.ModelUser
import com.alw.emarketshops.ui.cart.CartFragment
import com.alw.emarketshops.ui.home.HomeFragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_item_list.view.*
import kotlinx.android.synthetic.main.card_item_list.view.textViewName

class AdapterItemList(val arrayList: ArrayList<ModelItemCartList>, val context: CartFragment):RecyclerView.Adapter<AdapterItemList.ViewHolder>() {
    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        var qty=0
        fun  bindItemList(modelItemCard: ModelItemCartList){
            Picasso.get().load(modelItemCard.uri).resize(100,100).into(itemView.imageItemList)
            itemView.textViewName.text = modelItemCard.itemName
            itemView.textViewPrice.text = modelItemCard.itemPrice +" ฿"
            itemView.textViewQty.text = modelItemCard.itemQty + " ชิ้น"

            itemView.btnCartQtyUp.setOnClickListener {
                qty =Integer.parseInt(itemView.textViewQty.text.toString().substringBefore(" ชิ้น"))
                itemView.textViewQty.text = (qty+1).toString() + " ชิ้น"
                updateCartData()
            }
            itemView.btnCartQtydown.setOnClickListener {
                 qty =Integer.parseInt(itemView.textViewQty.text.toString().substringBefore(" ชิ้น"))
                itemView.textViewQty.text = (qty-1).toString() + " ชิ้น"
                updateCartData()
            }
        }

        fun updateCartData(){
            val data: MutableMap<String, Any> = hashMapOf(
                "brandId" to "brandId",
                "categoryCode" to "categoryCode",
                "categoryMainCode" to "categoryMainCode",
                "categorySubCode" to "categorySubCode",
                "date" to Timestamp.now(),
                "image" to "itemImg",
                "name" to "itemName",
                "price" to "itemPrice",
                "productId" to "productId",
                "qty" to qty,
                "shipping" to "shipping"
            )

            val productlist: MutableMap<String, Any> = hashMapOf(
                "0" to data
            )
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("cart_android_test").document(HomeFragment.Userdata.uid.toString())
            doc.update("productlist",FieldValue.arrayUnion(productlist))
                .addOnSuccessListener {
                Log.d("update qty", qty.toString())
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