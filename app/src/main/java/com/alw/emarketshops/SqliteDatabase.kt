package com.alw.emarketshops

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.alw.emarketshops.ui.cart.CartFragment
import com.google.firebase.firestore.FirebaseFirestore

class SqliteDatabase(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Database"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE TABLE_CART ('id' Integer PRIMARY KEY AUTOINCREMENT,'cartId' TEXT)"
        db?.execSQL(createTable)
        db?.execSQL("INSERT INTO TABLE_CART ('cartId') VALUES('0000')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + "TABLE_CART")
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun getCartId():String{
        var id =""
        val db = this.readableDatabase
        val selectQuery ="select * from TABLE_CART WHERE id=1"
        val cursor: Cursor
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.close()
            return id
        }
        Log.d("cursor.count ",cursor.count.toString())
        if (cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndex("cartId"))
//            Log.d("row id ",cursor.getString(cursor.getColumnIndex("id")))
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("cart_android_test").document(id)
            doc.get().addOnSuccessListener { documentSnapshot ->{
            }
            }
        }
        db.close()
        return  id
    }

    fun updateCartID(id:String){
        Log.d("updateCartID ",  id)
        val db = this.readableDatabase
        val rowid=1
        val contentValues = ContentValues()
        contentValues.put("cartId",id)
        db.update("TABLE_CART", contentValues, "id="+rowid,null)
        db.close()
    }
}