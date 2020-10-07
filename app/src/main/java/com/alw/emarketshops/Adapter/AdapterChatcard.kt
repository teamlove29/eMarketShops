package com.alw.emarketshops.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.alw.emarketshops.Activity.ActivityChat
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelChatCard
import com.alw.emarketshops.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.card_chat.view.*

class AdapterChatcard(val arrayList: ArrayList<ModelChatCard>, val context: Context):
    RecyclerView.Adapter<AdapterChatcard.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var brand :String
        lateinit var brandId :String
        lateinit var productId:String
        fun bindCahtList(modelChatCard: ModelChatCard) {
            itemView.textChatUsername.text = modelChatCard.name
            itemView.textChat.text = modelChatCard.chat
            itemView.textChatTime.text = modelChatCard.time
            brand = modelChatCard.name
            brandId = modelChatCard.brandId
            productId = modelChatCard.productId
            itemView.icoUnread.isVisible = false
//            Picasso.get().load(modelChatCard.uri).resize(100, 100).into(itemView.imageViewUserChat)
            getUnread(brandId)
        }

        fun getUnread(brandId:String){

            FirebaseDatabase.getInstance()
                .reference
                .child("messages")
                .child("unread-Messages")
                .child(FirebaseController.Userdata.uid.toString())
                .child(brandId)

                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.d("unread id",snapshot.value.toString())
                        itemView.icoUnread.isVisible = true

                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        itemView.icoUnread.isVisible = true
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        itemView.icoUnread.isVisible = false
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_chat,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCahtList(arrayList[position])
        holder.itemView.setOnClickListener {
            val inten = Intent(context, ActivityChat::class.java)
            inten.putExtra("brand", holder.brand)
            inten.putExtra("brandId", holder.brandId)
            inten.putExtra("productId", holder.productId)
            context.startActivity(inten)
            holder.itemView.icoUnread.isVisible =false
        }
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }


}