package com.alw.emarketshops

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.alw.emarketshops.ui.message.User
import com.github.bassaer.chatmessageview.model.Message
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_message.mChatView
import kotlin.random.Random

class ActivityChat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val i = intent
        textBrandChat.text = i.getStringExtra("brand")
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        val myName = FirebaseController.Userdata.name.toString()

        val adminIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_user)

        val shopAdmin = i.getStringExtra("brand")
        val me = User(0, myName, myIcon)
        val admin = User(1, shopAdmin, adminIcon)

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
//        mChatView.setOnClickOptionButtonListener(View.OnClickListener{
////            openGallery()
//        })

        mChatView.setOnClickSendButtonListener(View.OnClickListener { //new message
            val message: Message = Message.Builder()
                .setUser(me)
                .setRight(true)
                .setText(mChatView.inputText)
                .setPicture(myIcon)
                .setType(Message.Type.TEXT)
                .hideIcon(true)
                .build()
            val txt = mChatView.inputText
            val resaultText: String
            mChatView.send(message)
            mChatView.inputText = ""

            //Receive message
            resaultText = if (txt.contains("ดี") ){
                "สวัสดี $myName"
            }else{
                "เราไม่เข้าใจข้อความของคุณ"
            }
            val receivedMessage: Message = Message.Builder()
                .setUser(admin)
                .setRight(false)
                .setText(resaultText)
                .build()

            /*
            This is a demo bot
            Return within 3 seconds
            */
            val sendDelay: Int = (Random.nextInt(1) + 1) * 1000
            Handler().postDelayed(
                { mChatView.receive(receivedMessage) },
                sendDelay.toLong()
            )
        })
    }
}