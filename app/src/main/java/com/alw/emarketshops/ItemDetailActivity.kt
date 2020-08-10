package com.alw.emarketshops

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_card.view.*
import java.time.LocalDateTime
import java.util.*

class ItemDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    lateinit var itemName:String
    lateinit var itemPrice:String
    var itemQty:String = "1"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val i = intent
        textItemName.text = i.getStringExtra("itemName")
        itemName = i.getStringExtra("itemName")
        textItemPrice.text = "฿ " + i.getStringExtra("itemPrice")
        itemPrice = i.getStringExtra("itemPrice")
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
            addDataCart()
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
                itemQty = textDetailItemQty.text.toString()
            }
        }
        btnDetailQtyUp.setOnClickListener {
            textDetailItemQty.text = (Integer.parseInt(textDetailItemQty.text.toString())+1).toString()
            itemQty = textDetailItemQty.text.toString()
        }
    }
    private val db = FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDataCart(){

        val itemdata = hashMapOf(
            "brandId" to "",
            "categoryCode" to "C001",
            "categoryMainCode" to "CM004",
            "categorySubCode" to "",
            "date" to LocalDateTime.now().toString(),
            "image" to "",
            "name" to itemName,
            "price" to itemPrice,
            "qty" to itemQty,
            "shipping" to ""
        )
        val cartdata = listOf(itemdata)
        val productList = hashMapOf(
            "productlist" to cartdata
        )

        db.collection("cart_android_test")
            .add(productList as Map<String,Any>)
            .addOnSuccessListener {
                Log.d("TAG", "addDataCart: " + it.id)
            }
            .addOnFailureListener{
                Log.d("TAG", "addDataCart error: ")
            }

    }

}