package com.alw.emarketshops.Model

import android.graphics.Bitmap
import com.github.bassaer.chatmessageview.model.IChatUser

class User(id: Int, name: String, icon: Bitmap): IChatUser {
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
