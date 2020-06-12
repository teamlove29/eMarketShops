package com.alw.emarketshops

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val i = intent
        textItemName.text = i.getStringExtra("itemName")
        textItemPrice.text = "฿ " + i.getStringExtra("itemPrice")
        imageViewitem!!.setImageResource(i.getIntExtra("itemImg",0))

        btnAddtoCart.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("แจ้งเตือน")
            dialogBuilder.setMessage("เพิ่มสินค้าไปยังตะกร้าเรียบร้อย")
            dialogBuilder.setPositiveButton("ตกลง", DialogInterface.OnClickListener { _, _ ->})
            dialogBuilder.show()
        }
    }
}