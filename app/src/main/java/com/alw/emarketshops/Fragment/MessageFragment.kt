package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.Adapter.AdapterChatcard
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelChatCard
import com.alw.emarketshops.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_message.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MessageFragment : Fragment() {
    var brand = ""
    var productId = ""
    val  arrayList = ArrayList<ModelChatCard>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        val context: Context? = this.context
        listViewChat.layoutManager  = GridLayoutManager(
            context,
            1,
            GridLayoutManager.VERTICAL,
            false
        )

        getChilEvenMessage()

    }

    fun getUnread(snapshot: DataSnapshot,brandId:String){

        FirebaseDatabase.getInstance()
            .reference
            .child("messages")
            .child("unread-Messages")
            .child(uid.toString())
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
            .getReference("messages").child(uid!!)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    println(snapshot.key.toString())
                    val brandId = snapshot.key.toString()

                    val task = FirebaseController().getShopData(brandId)
                    task?.addOnSuccessListener {

                        val brand = it["shopName"].toString()
                        val uri: Uri = Uri.parse(it["logo"].toString())
                        var product = ""

                        val ref =FirebaseDatabase.getInstance()
                            .getReference("messages")
                            .child(uid.toString())
                            .child(brandId)
                            .orderByKey().limitToLast(1)
                        ref.addChildEventListener(object : ChildEventListener {
                            override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                val time:String = getDateTime(snapshot.child("time").value.toString().toLong())
                                val chat: String = if (snapshot.child("type").value == null){
                                    snapshot.child("message").value.toString()
                                }else{
                                    "send image"
                                }
                                val refProduct =FirebaseDatabase.getInstance()
                                    .getReference("messages")
                                    .child(uid.toString())
                                    .child(brandId)
                                    .orderByKey().limitToFirst(1)
                                refProduct.addChildEventListener(object : ChildEventListener {
                                    override fun onChildAdded(
                                        snapshot: DataSnapshot,
                                        previousChildName: String?
                                    ) {
                                        product = snapshot.child("productId").value.toString()

                                        println("product $product")
//                                        arrayList.
                                        arrayList.add(ModelChatCard(brand,chat,time,uri,brandId,product))

                                        val adapterChatcard = AdapterChatcard(arrayList, this@MessageFragment)
                                        listViewChat.adapter = adapterChatcard
                                    }

                                    override fun onChildChanged(
                                        snapshot: DataSnapshot,
                                        previousChildName: String?
                                    ) {
                                    }

                                    override fun onChildRemoved(snapshot: DataSnapshot) {
                                    }

                                    override fun onChildMoved(
                                        snapshot: DataSnapshot,
                                        previousChildName: String?
                                    ) {
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })


                            }

                            override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                            }

                            override fun onChildRemoved(snapshot: DataSnapshot) {
                            }

                            override fun onChildMoved(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })


                    }

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


