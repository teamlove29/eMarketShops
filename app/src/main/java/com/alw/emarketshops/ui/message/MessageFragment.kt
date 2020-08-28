package com.alw.emarketshops.ui.message

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.R
import com.github.bassaer.chatmessageview.model.IChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.Message.Builder
import kotlinx.android.synthetic.main.fragment_message.*
import java.io.IOException
import kotlin.random.Random


class MessageFragment : Fragment() {
    private val READ_REQUEST_CODE = 100
    val me: User? = null
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
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        val myName = FirebaseController.Userdata.name.toString()

        val yourId = 1
        val yourIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_user)
        val yourName = "Admin"
        val me = User(myId, "", myIcon)
        val you = User(yourId, yourName, yourIcon)


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
        mChatView.setMessageMarginTop(4)
        mChatView.setMessageMarginBottom(4)
        mChatView.setOnClickOptionButtonListener(View.OnClickListener{
            openGallery()
        })


        mChatView.setOnClickSendButtonListener(View.OnClickListener { //new message
            val message: Message = Builder()
                .setUser(me)
                .setRight(true)
                .setText(mChatView.inputText)
                .setPicture(myIcon)
                .setType(Message.Type.TEXT)
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
//    private fun showDialog() {
//        val items = arrayOf(
//            getString(R.string.send_picture),
//            getString(R.string.clear_messages)
//        )
//        Builder()
//            .setTitle(getString(R.string.options))
//            .setItems(items,
//                DialogInterface.OnClickListener { dialogInterface, position ->
//                    when (position) {
//                        0 -> openGallery()
//                        1 -> mChatView.getMessageView().removeAll()
//                    }
//                })
//            .show()
//    }
    private fun openGallery() {
        val intent: Intent
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = Intent(Intent.ACTION_GET_CONTENT)
        } else {
            intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.type = "image/*"
        startActivityForResult(intent, 100)


    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode !== READ_REQUEST_CODE || resultCode !== RESULT_OK || attr.data == null) {
//            return
//        }
//        val uri: Uri = attr.data
        try {
//            val picture = MediaStore.Images.Media.getContentUri(uri.toString())
            val message = me?.let {
                Builder()
                    .setRight(true)
                    .setText(Message.Type.PICTURE.name)
                    .setUser(it)
                    .hideIcon(true)
                    .setPicture(BitmapFactory.decodeResource(resources, R.drawable.face_2))
                    .setType(Message.Type.PICTURE)
        //                .setStatusIconFormatter(MyMessageStatusFormatter(this@MessengerActivity))
        //                .setStatusStyle(Message.Companion.getSTATUS_ICON())
        //                .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                    .build()
            }
            if (message != null) {
                mChatView.send(message)
            }
            //Add message list
//            mMessageList.add(message)
//            receiveMessage(Message.Type.PICTURE.name())
        } catch (e: IOException) {
            e.printStackTrace()
//            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
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
