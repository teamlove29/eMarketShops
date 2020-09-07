package com.alw.emarketshops.Activity

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Fragment.User
import com.alw.emarketshops.Model.ChatMessage
import com.alw.emarketshops.R
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.util.IMessageStatusIconFormatter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_message.mChatView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ActivityChat : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    val myID  = FirebaseController.Userdata.uid.toString()
    var brandId = ""
    var shopAdmin = ""
    var admin:User? = null
    var me:User? = null
    val testid = "evOyts06TESNIqHKH61WMLdDDvV2_k9wziMUVkthE9YZ8tBfjP84cN802"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

//        val timeStamp:String = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()

        val i = intent
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        brandId = i.getStringExtra("brandId")
        val adminIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_user)
        shopAdmin = i.getStringExtra("brand")

        me = User(0, FirebaseController.Userdata.name.toString(), myIcon)
        admin = User(1, shopAdmin, adminIcon)

        getChilEvenMessage()

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

        mChatView.setOnClickSendButtonListener(View.OnClickListener { //new message

            val txt = mChatView.inputText
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val timeStamp: String = dateFormat.format(Date())
            val chatMessage = ChatMessage(
                FirebaseController.Userdata.name.toString(),
                txt,
                timeStamp,
                myID
            )

            FirebaseDatabase.getInstance()
                .reference
                .child("chat")
                .child(testid)
//                .child(myID + "_" + brandId)  //** Chat Room
                .push()
                .setValue(chatMessage)

            mChatView.inputText = ""
        })
    }
    fun getChilEvenMessage(){
        FirebaseDatabase.getInstance()
            .getReference("chat").child(testid)
            .addChildEventListener(object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    getDataSnaphot(snapshot)
            }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("postSnapshot >>","onDataChange")

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun  getDataSnaphot(snapshot:DataSnapshot){
        if (snapshot.child("uid").value == myID){
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val sendtime = Calendar.getInstance()
            val date = df.parse(snapshot.child("timestamp").value.toString())
            sendtime.time = date

            val receivedMessage: Message = Message.Builder()
                .setUser(me!!)
                .setUsernameVisibility(false)
                .setSendTime(sendtime)
                .setRight(true)
                .hideIcon(true)
                .setText(snapshot.child("message").value.toString())
                .build()
            mChatView.send(receivedMessage)
        }else{
            val adminIcon = BitmapFactory.decodeResource(
                resources,
                R.drawable.baseline_account_circle_black_24dp
            )
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val sendtime = Calendar.getInstance()
            val date = df.parse(snapshot.child("timestamp").value.toString())
            sendtime.time = date
            val sdUser = User(1, snapshot.child("userchat_name").value.toString(), adminIcon)
            val receivedMessage: Message = Message.Builder()
                .setUser(sdUser!!)
                .setSendTime(sendtime)
                .setRight(false)
                .setText(snapshot.child("message").value.toString())
                .build()
            mChatView.receive(receivedMessage)

            val snoozeIntent = Intent(this, ActivityChat::class.java).apply {
                action = "ACTION_SNOOZE"
                putExtra(EXTRA_NOTIFICATION_ID, 0)
            }
            val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
            val builder = NotificationCompat.Builder(this, 0.toString())
                .setSmallIcon(R.drawable.ic_user)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .addAction(R.drawable.ic_action_send, getString(R.string.project_id),
                    snoozePendingIntent)
            builder.build()

        }
    }

}