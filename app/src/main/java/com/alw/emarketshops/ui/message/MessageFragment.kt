package com.alw.emarketshops.ui.message

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import com.alw.emarketshops.R
import com.alw.emarketshops.ui.home.HomeFragment
import com.github.bassaer.chatmessageview.model.IChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.Message.Builder
import com.github.bassaer.chatmessageview.util.ChatBot.talk
import kotlinx.android.synthetic.main.fragment_message.*
import kotlin.random.Random


class MessageFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        val myId = 0
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_1)
        val myName = HomeFragment.Userdata.name.toString()

        val yourId = 1
        val yourIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_user)
        val yourName = "Admin"
        val me = User(myId, "", myIcon)
        val you = User(yourId, yourName, yourIcon)
       val mChatView = chat_view

        //Set UI parameters if you need
        //Set UI parameters if you need
        val context = this.context
        context?.let { ContextCompat.getColor(it, R.color.green500) }?.let {
            mChatView.setRightBubbleColor(
                it
            )
        }
        context?.let { ContextCompat.getColor(it, R.color.gray300) }?.let {
            mChatView.setLeftBubbleColor(
                it
            )
        }
        context?.let { ContextCompat.getColor(it, R.color.with) }?.let {
            mChatView.setBackgroundColor(
                it
            )
        }
        context?.let { ContextCompat.getColor(it, R.color.green500) }?.let {
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
        mChatView.setMessageMarginTop(5)
        mChatView.setMessageMarginBottom(5)

        mChatView.setOnClickSendButtonListener(View.OnClickListener { //new message
            val message: Message = Builder()
                .setUser(me)
                .setRight(true)
                .setText(mChatView.inputText)
                .hideIcon(true)
                .build()
            val txt = mChatView.inputText
            var resaultText: String
            mChatView.send(message)
            //Reset edit text
            mChatView.inputText = ""

            //Receive message
            resaultText = if (txt.contains("hello") ){
                "Hello $myName"
            }else{
                "?"
            }
            val receivedMessage: Message = Builder()
                .setUser(you)
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
class User(id:Int, name:String, icon:Bitmap): IChatUser {
    internal var id:Int = id
    private var name:String = name
    private var icon: Bitmap = icon

    override fun getName(): String? {
        return  this.name
    }


    override fun setIcon(bmp: Bitmap) {
//        return this.icon
    }

    override fun getIcon(): Bitmap? {
        return  this.icon
    }

    override fun getId():String {
        return this.id.toString()
    }
}
