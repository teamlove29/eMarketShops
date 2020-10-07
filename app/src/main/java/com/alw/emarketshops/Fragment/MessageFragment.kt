package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterChatcard
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ModelChatCard
import com.alw.emarketshops.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.card_chat.view.*
import kotlinx.android.synthetic.main.fragment_message.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MessageFragment : Fragment() {
    var brand = ""
    var productId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        getChatList()
//        getChilEvenMessage()

    }
    fun getChatList(){
        val context: Context? = this.context
        listViewChat.layoutManager  = GridLayoutManager(
            context,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        FirebaseDatabase.getInstance()
            .getReference("messages").child(FirebaseController.Userdata.uid.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d("snapshot>>>",snapshot.key.toString())
                    val brandId = snapshot.key.toString()

                    val ref =FirebaseDatabase.getInstance()
                        .getReference("messages")
                        .child(FirebaseController.Userdata.uid.toString())
                        .child(brandId)
                        .orderByKey().limitToLast(1)
                    val refProduct =FirebaseDatabase.getInstance()
                        .getReference("messages")
                        .child(FirebaseController.Userdata.uid.toString())
                        .child(brandId)
                        .orderByKey().limitToFirst(1)
                        .addChildEventListener(object : ChildEventListener{
                            override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                productId = snapshot.child("productId").value.toString()
                            }

                            override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                TODO("Not yet implemented")
                            }

                            override fun onChildRemoved(snapshot: DataSnapshot) {
                                TODO("Not yet implemented")
                            }

                            override fun onChildMoved(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                TODO("Not yet implemented")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    FirebaseController().getShopData(brandId)?.addOnSuccessListener { it ->
                        brand = it["shopName"].toString()

                        ref.addChildEventListener(object : ChildEventListener {
                            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                                val  arrayList = ArrayList<ModelChatCard>()
                                arrayList.clear()
                                val adapterChatcard = context?.let { AdapterChatcard(arrayList, it) }
                                val chat = snapshot.child("message").value.toString()
                                val time:String = getDateTime(snapshot.child("time").value.toString().toLong())

                                arrayList.add(ModelChatCard(brand,chat,time,null,brandId,productId))
                                try {
                                    listViewChat.adapter = adapterChatcard
                                }catch (e:Exception){
                                    Log.d("",e.message)
                                }


                            }

                            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d("postSnapshot >>", "onDataChange")

                            }

                            override fun onChildRemoved(snapshot: DataSnapshot) {

                            }

                            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                    }






                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Log.d("","onChildRemoved")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }

                })



    }
    fun getUnread(snapshot: DataSnapshot,brandId:String){

        FirebaseDatabase.getInstance()
            .reference
            .child("messages")
            .child("unread-Messages")
            .child(FirebaseController.Userdata.uid.toString())
            .child(brandId)
            .child(snapshot.child("messageId").value.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    private fun getDateTime(time: Long): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val netDate = Date(time)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

   private fun getChilEvenMessage(){
        FirebaseDatabase.getInstance()
            .getReference("messages")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    getDataSnaphot(snapshot)
//                    Log.d("snapshot message>>",snapshot.key.toString().substringBefore("_"))
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("postSnapshot >>", "onDataChange")

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}


