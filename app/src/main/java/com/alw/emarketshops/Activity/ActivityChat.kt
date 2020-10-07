package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Model.ChatMessage
import com.alw.emarketshops.Model.User
import com.alw.emarketshops.R
import com.github.bassaer.chatmessageview.model.Message
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.card_item_list.view.*
import java.util.*


class ActivityChat : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    var myID  = ""
    var brandId = ""
    var shopAdmin = ""
    var admin: User? = null
    var me:User? = null
    var productId:String? = null
    var hasproductId:Boolean=false
    var imageUri = ""
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        if (FirebaseController.Userdata.uid !== null){
            myID  = FirebaseController.Userdata.uid.toString()
        }else{
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }

        crdProduct.isVisible = false
        val i = intent
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        val adminIcon = BitmapFactory.decodeResource(
            resources, R.drawable.baseline_account_circle_black_24dp
        )
        brandId = i.getStringExtra("brandId")
        shopAdmin = i.getStringExtra("brand")
        productId = i.getStringExtra("productId").toString()
        me = User(0, FirebaseController.Userdata.name.toString(), myIcon)
        admin = User(1, shopAdmin, adminIcon)
        db.collection("product").document(productId!!)
            .get()
            .addOnCompleteListener {
                val map: MutableMap<String, Any>? = it.result?.data

                val ls = map?.get("images") as ArrayList<Any>
                for ((index, each) in ls.withIndex()){
                    val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                    if (imgdata !== null && index == 0){
                        imageUri = imgdata["imgUrl"].toString()
                        crdProduct.isVisible = true
                        Picasso.get().load(imageUri).into(imgProduct)
                    }
                }

                textProductname.text = map["name"].toString() //i.getStringExtra("itemName")
                getChilEvenMessage()
            }


        textBrandChat.text = i.getStringExtra("brand")

        this.let { ContextCompat.getColor(it, R.color.green500) }?.let {
            mChatView.setRightBubbleColor(
                it
            )
        }
        this.let { ContextCompat.getColor(it, R.color.gray300) }?.let {
            mChatView.setLeftBubbleColor(
                it
            )
        }
        this.let { ContextCompat.getColor(it, R.color.with) }?.let {
            mChatView.setBackgroundColor(
                it
            )
        }
        this.let { ContextCompat.getColor(it, R.color.green500) }?.let {
            mChatView.setSendButtonColor(
                it
            )
        }
        mChatView.setSendIcon(R.drawable.ic_action_send)
        mChatView.setRightMessageTextColor(Color.WHITE)
        mChatView.setLeftMessageTextColor(Color.DKGRAY)
        mChatView.setUsernameTextColor(Color.BLACK)
        mChatView.setSendTimeTextColor(Color.BLACK)
        mChatView.setDateSeparatorColor(Color.BLACK)
        mChatView.setInputTextHint("new message...")
        mChatView.setMessageMarginTop(4)
        mChatView.setMessageMarginBottom(4)
        mChatView.setEnableSwipeRefresh(true)
//        mChatView.setRefreshing(true)
        mChatView.scrollToEnd()
//        mChatView.setOnClickOptionButtonListener(View.OnClickListener{
////            openGallery()
//        })
        //***
//        if (imageUri !== null){
//            val picMessage: Message = Message.Builder()
//                .setUser(me!!)
//                .setUsernameVisibility(false)
//                .setRight(true)
//                .hideIcon(true)
//                .setType(Message.Type.PICTURE)
//                .setPicture(urlToBit(imageUri)!!)
//                .build()
//            mChatView.send(picMessage)
//        }

        mChatView.setOnClickSendButtonListener(View.OnClickListener { //new message
            val currentDate = ServerValue.TIMESTAMP
            if (mChatView.inputText !== "") {

                val dbRef = FirebaseDatabase.getInstance()
                    .reference
                    .child("messages")
                    .child(myID)
                    .child(brandId)
                val key_product = dbRef.push().key.toString()
                val key = dbRef.push().key.toString()

                //*** Send Product data
                if (!hasproductId){
                    sendProductdata(productId,key,key_product)
                }


                val chatMessage = ChatMessage(
                    mChatView.inputText,
                    key,
                    brandId,
                    FirebaseController.Userdata.uid.toString(),
                    currentDate
                )


                dbRef.child(key).setValue(chatMessage)

               val chatMessage2 = ChatMessage(
                    mChatView.inputText,
                    key,
                    brandId,
                    FirebaseController.Userdata.uid.toString(),
                    currentDate
                )
                val dbRef2 = FirebaseDatabase.getInstance()
                    .reference
                    .child("messages")
                    .child(brandId)
                    .child(myID)
                dbRef2.child(key).setValue(chatMessage2)



                val friendsList: HashMap<String, Any> = HashMap()
                friendsList[brandId] = true

              val ref_f1 =  FirebaseDatabase.getInstance()
                    .reference
                    .child("friendsList")
                    .child(myID).child(brandId)
                ref_f1.setValue(friendsList)

                friendsList[myID] = true
                val ref_f2 = FirebaseDatabase.getInstance()
                    .reference
                    .child("friendsList")
                    .child(brandId).child(myID)
                ref_f2.setValue(friendsList)

                val unread: HashMap<String, Any> = HashMap()
                unread[key] = 1

               val ref_unread= FirebaseDatabase.getInstance()
                    .reference
                    .child("messages")
                    .child("unread-Messages")
                    .child(brandId)
                    .child(myID)
                    .child(key)
                ref_unread.setValue(unread)



                mChatView.inputText = ""
            }
        })

    }
    fun sendProductdata(productId:String?,key:String,key_product:String){

        val products: HashMap<String, Any> = HashMap()
        products["messageId"] = key
        products["productId"] = productId.toString()
        products["recipient"] = brandId
        products["sender"] = myID
        products["time"] = ServerValue.TIMESTAMP

        val ref1= FirebaseDatabase.getInstance()
            .reference
            .child("messages")
            .child(brandId)
            .child(myID)
            .child(key_product)
        ref1.setValue(products)

        val ref2= FirebaseDatabase.getInstance()
            .reference
            .child("messages")
            .child(myID)
            .child(brandId)
            .child(key_product)
        ref2.setValue(products)
    }

    fun getChilEvenMessage(){
        FirebaseDatabase.getInstance()
            .getReference("messages").child(myID).child(brandId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    getDataSnaphot(snapshot)
                    getProductMessage(snapshot)
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

    @SuppressLint("SetTextI18n")
    fun getProductMessage(snapshot: DataSnapshot):Boolean{
        if (snapshot.child("productId").value !== null ){
            hasproductId = true
            println(snapshot.child("productId").value.toString())
            db.collection("product")
                .document(snapshot.child("productId").value.toString())
                .get()
                .addOnCompleteListener {

                    val map: MutableMap<*, *>? = it.result!!.data
                    if (map != null) {
                       textProductname.text = map["name"].toString()+ " " + map["price"].toString()+"฿"
                        var uri: Uri
                        val dt:Any? = map["images"]
                        val ls = dt as ArrayList<*>
                        for ((index, each) in ls.withIndex()){
                            val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                            if (imgdata !== null && index == 0){
                                uri = Uri.parse(imgdata["imgUrl"].toString())
                                imageUri = imgdata["imgUrl"].toString()
                                Picasso.get().load(uri).into(imgProduct)

                            }
                        }
                    }


                }

            crdProduct.isVisible = true
        }
        return hasproductId
    }


    fun  getDataSnaphot(snapshot: DataSnapshot){
        if (snapshot.child("sender").value == myID){
            if (snapshot.child("message").value !== null){
            val receivedMessage: Message = Message.Builder()
                .setUser(me!!)
                .setUsernameVisibility(false)
                .setSendTime(getDate(snapshot.child("time").value as Long))
                .setRight(true)
                .hideIcon(true)
                .setText(snapshot.child("message").value.toString())
                .build()
            mChatView.send(receivedMessage)
            }
//            else if (snapshot.child("productId").value !== null){
//                val picMessage: Message = Message.Builder()
//                    .setUser(me!!)
//                    .setUsernameVisibility(false)
//                    .setSendTime(getDate(snapshot.child("time").value as Long))
//                    .setRight(true)
//                    .hideIcon(true)
//                    .setType(Message.Type.PICTURE)
//                    .setPicture(urlToBit(imageUri)!!)
//                    .build()
//                mChatView.send(picMessage)
//                val productMessage: Message = Message.Builder()
//                    .setUser(me!!)
//                    .setUsernameVisibility(false)
//                    .setSendTime(getDate(snapshot.child("time").value as Long))
//                    .setRight(true)
//                    .hideIcon(true)
//                    .setText(textProductname.text.toString())
//                    .build()
//
//                mChatView.send(productMessage)
//            }
        }else{
            if (snapshot.child("message").value !== null) {
                val receivedMessage: Message = Message.Builder()
                    .setUser(admin!!)
                    .setSendTime(getDate(snapshot.child("time").value as Long))
                    .setRight(false)
                    .setStatus(0)
                    .setText(snapshot.child("message").value.toString())
                    .build()
                mChatView.receive(receivedMessage)
                mChatView.updateMessageStatus(receivedMessage, 1)
            }
            Log.d("mss id", snapshot.child("messageId").value.toString())
            FirebaseDatabase.getInstance()
                .reference
                .child("messages")
                .child("unread-Messages")
                .child(myID)
                .child(brandId)
                .child(snapshot.child("messageId").value.toString())
                .removeValue()
        }

    }
    fun getDate(timestamp: Long) : Calendar {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp
        return calendar
    }

//    fun notifi(message: String){
//        val bitmap = BitmapFactory.decodeResource(
//            this.getResources(),
//            R.drawable.emarket_logo
//        )
//        val NOTIFICATION_ID = 234
//        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            val CHANNEL_ID = "eMarketShops"
//            val name = "eMarketShops"
//            val Description = "eMarketShops Chat"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
//            mChannel.setDescription(Description)
//            mChannel.enableLights(true)
//            mChannel.setLightColor(Color.RED)
//            mChannel.enableVibration(true)
//            mChannel.setVibrationPattern(
//                longArrayOf(
//                    100,
//                    200,
//                    300,
//                    400,
//                    500,
//                    400,
//                    300,
//                    200,
//                    400
//                )
//            )
//            mChannel.setShowBadge(false)
//            notificationManager.createNotificationChannel(mChannel)
//        }
//
//        val builder = NotificationCompat.Builder(this, "my_channel_01")
//            .setSmallIcon(R.drawable.ic_report)
//            .setContentTitle("New message")
//            .setLargeIcon(bitmap)
//            .setContentText(message)
//        val resultIntent = Intent(this, ActivityChat::class.java)
//        resultIntent.putExtra("brand", shopAdmin)
//        resultIntent.putExtra("brandId", brandId)
//        val stackBuilder = TaskStackBuilder.create(this)
//        stackBuilder.addParentStack(ActivityChat::class.java)
//        stackBuilder.addNextIntent(resultIntent)
//        val resultPendingIntent = stackBuilder.getPendingIntent(
//            0,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        builder.setContentIntent(resultPendingIntent)
//        notificationManager.notify(NOTIFICATION_ID, builder.build())
//    }

    fun urlToBit(url:String):Bitmap?{
        var bt:Bitmap? = null
        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bt = bitmap
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })

        return bt
    }

}