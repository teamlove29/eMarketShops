package com.alw.emarketshops

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_card.view.*

class ItemDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val i = intent
        textItemName.text = i.getStringExtra("itemName")
        textItemPrice.text = "฿ " + i.getStringExtra("itemPrice")
        textItemStock.text = "จำนวนสินค้า " + i.getStringExtra("itemStock")
        textItemDetail.text = "รายละเอียด " + i.getStringExtra("itemDetail")
        textItemBrand.text = "แบรนด์ " + i.getStringExtra("itemBrand")
        val uri: Uri = Uri.parse(i.getStringExtra("itemImg"))
        Picasso.get().load(uri).into(imageViewitem)

        // Test Get All Product Data **
        val id = i.getStringExtra("id")
        val db = FirebaseFirestore.getInstance()
        db.collection("product").document(id)
            .get()
            .addOnCompleteListener { task  ->
                Log.d("categoryCode :", task.result?.get("categoryName") as String)
                Log.d("data :", task.result?.data.toString())
                textItemBrand.text ="แบรนด์ :" + task.result?.get("brand") as String +
                        ", ชนิด :" + task.result?.get("categoryName") as String

            }
        // **

        btnAddtoCart.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("แจ้งเตือน")
            dialogBuilder.setMessage("เพิ่มสินค้าไปยังตะกร้าเรียบร้อย")
            dialogBuilder.setPositiveButton("ตกลง") { _, _ ->}
            dialogBuilder.show()
        }
        btnDetailQtyDown.setOnClickListener {
            textDetailItemQty.text = (Integer.parseInt(textDetailItemQty.text.toString())-1).toString()
            if (Integer.parseInt(textDetailItemQty.text.toString()) < 1){
                textDetailItemQty.text = "1"
            }
        }
        btnDetailQtyUp.setOnClickListener {
            textDetailItemQty.text = (Integer.parseInt(textDetailItemQty.text.toString())+1).toString()
        }
    }
}