package com.alw.emarketshops.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityCategory
import com.alw.emarketshops.Activity.ActivitySubCategory
import com.alw.emarketshops.Activity.ActivityWorldInc
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelCategoryCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_card.view.*

class AdapterCategoryCard(val arrayList: ArrayList<ModelCategoryCard>, val context: ActivityCategory):RecyclerView.Adapter<AdapterCategoryCard.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var code:String=""
        var cateName:String=""
        var img: Uri? =null
        fun  bindCates(modelCategory: ModelCategoryCard){
            if (modelCategory.img.toString() !== "") {
                Picasso.get().load(modelCategory.img)
//                    .resize(80,80)
                    .into(itemView.imgCateCard)
            }
//            itemView.imgCateCard.setImageResource(R.drawable.e_market_shops_bw)
            itemView.textViewCateName.text  = modelCategory.nameTH
            code = modelCategory.cateCode
            cateName = modelCategory.nameTH
            img = modelCategory.img
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCates(arrayList[position])
        holder.itemView.setOnClickListener {
//          context.getSubCategory(holder.code,holder.cateName)

            insertCategoryLastView(
                ModelCategoryCard(holder.code,holder.code,holder.cateName,holder.img!!)
            )
            if (holder.code == "C005"){
                val inten =Intent(context, ActivityWorldInc::class.java)
                inten.putExtra("code",holder.code)
                context.startActivity(inten)
            }else{
            val i = Intent(context, ActivitySubCategory::class.java)
                i.putExtra("code",holder.code)
                i.putExtra("cateName",holder.cateName)
                context.startActivity(i)
                context.finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun insertCategoryLastView(modelCategoryCard: ModelCategoryCard){
        println(modelCategoryCard.nameTH)

        val data = hashMapOf(
            "nameTH" to modelCategoryCard.nameTH,
            "cateCode" to modelCategoryCard.cateCode,
            "mainCateCode" to modelCategoryCard.mainCateCode,
            "src" to modelCategoryCard.img.toString()
        )
        val category = hashMapOf(
            "category" to listOf(data)
        )

       val ref= db.collection("category_last_view").document(uid!!)
            ref.get().addOnSuccessListener {
            if (it.data !== null){

                ref.update("category", FieldValue.arrayRemove(data))
                ref.update("category", FieldValue.arrayUnion(data))
            }else{
                ref.set(category)
            }

            }
    }

}