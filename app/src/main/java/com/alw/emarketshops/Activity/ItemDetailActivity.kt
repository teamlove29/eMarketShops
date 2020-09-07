package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*


class ItemDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    lateinit var itemName:String
    lateinit var itemPrice:String
    lateinit var itemImg :String
    lateinit var brand :String
    lateinit var brandId :String
    lateinit var categoryName:String
    lateinit var categoryCode:String
    lateinit var categoryMainCode:String
    lateinit var categorySubCode:String
    lateinit var productId:String
    lateinit var shopName:String
    lateinit var shipping:Any
    private val firebaseController = FirebaseController()
    var cartId: String? = null
    var itemQty:String = "1"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        checkCardId(FirebaseController.Userdata.uid.toString())

        val i = intent
        textItemName.text = i.getStringExtra("itemName")
        itemName = i.getStringExtra("itemName")
        textItemPrice.text = "฿ " + i.getStringExtra("itemPrice")
        itemPrice = i.getStringExtra("itemPrice")
        textItemStock.text = "จำนวนสินค้า : " + i.getStringExtra("itemStock")
        textItemDetail.text = "รายละเอียด : " + i.getStringExtra("itemDetail")

        val uri: Uri = Uri.parse(i.getStringExtra("itemImg"))
        Picasso.get().load(uri).into(imageViewitem)
        itemImg = i.getStringExtra("itemImg")

        val id = i.getStringExtra("id")
        val db = FirebaseFirestore.getInstance()
        db.collection("product").document(id)
            .get()
            .addOnCompleteListener { taskproduct  ->
                textItemBrand.text ="แบรนด์ : " + taskproduct.result?.get("brand") as String
                textItemLocation.text = "ส่งจาก : " + taskproduct.result?.get("type") as String
                textItemType.text = "ชนิด : " + taskproduct.result?.get("categoryName") as String
                brandId = taskproduct.result?.get("userId").toString()
                categoryName = taskproduct.result?.get("categoryName").toString()
                categoryCode = taskproduct.result?.get("categoryCode").toString()
                categoryMainCode = taskproduct.result?.get("categoryMainCode").toString()
                categorySubCode = taskproduct.result?.get("categorySubCode").toString()
                brand = taskproduct.result?.get("brand").toString()
                productId = id

                val map: MutableMap<*, *>? = taskproduct.result!!.data  // shipping data
                if (map != null) {
                    val list: Any? = map.get("shipping")
                    if (list != null) {
                        shipping = list
                    }
                }

                db.collection("shops").document(taskproduct.result?.get("userId").toString())
                    .get().addOnCompleteListener { taskShop  ->
                        shopName = taskShop.result?.get("shopName").toString()
                    }
            }

        btnQuota.setOnClickListener {
            val i = Intent(this, ActivityReQuotation::class.java)
            i.putExtra("productName", itemName)
            i.putExtra("productId", productId)
            i.putExtra("categoryCode", categoryCode)
            i.putExtra("categoryMainCode", categoryMainCode)
            i.putExtra("categorySubCode", categorySubCode)
            i.putExtra("categoryName", categoryName)
            i.putExtra("brand", brand)
            i.putExtra("brandId", brandId)
            startActivity(i)
        }
        btnChat.setOnClickListener {
            val inten = Intent(this, ActivityChat::class.java)
            inten.putExtra("brand", brand)
            inten.putExtra("brandId", brandId)
            startActivity(inten)
        }


        btnAddtoCart.setOnClickListener{

            addDataCart()
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("")
            dialogBuilder.setMessage("เพิ่มสินค้าไปยังตะกร้าเรียบร้อย")
            dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                finish()
            }
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
        if (cartId == null){ // insert Cart
            val itemdata = hashMapOf(
                "brandId" to brandId,
                "categoryCode" to categoryCode,
                "categoryMainCode" to categoryMainCode,
                "categorySubCode" to categorySubCode,
                "date" to Timestamp.now(),
                "image" to itemImg,
                "name" to itemName,
                "price" to itemPrice,
                "productId" to productId,
                "qty" to itemQty,
                "shipping" to shipping
            )
            val productList = hashMapOf(
                "productlist" to listOf(itemdata)
            )

            db.collection(firebaseController.docCart).document(FirebaseController.Userdata.uid.toString())
                .set(productList as Map<*, *>)
                .addOnSuccessListener {
                    Log.d("TAG", "Success insert DataCart: ")
                    finish()
                }
                .addOnFailureListener{
                    Log.d("TAG", "addDataCart error: ")
                }

        }else{ // update Cart
            cartdataPut(cartId!!)

        }

    }
    fun cartdataPut(cartId: String){


            val data: MutableMap<String, Any> = hashMapOf(
                "brandId" to brandId,
                "categoryCode" to categoryCode,
                "categoryMainCode" to categoryMainCode,
                "categorySubCode" to categorySubCode,
                "date" to Timestamp.now(),
                "image" to itemImg,
                "name" to itemName,
                "price" to itemPrice,
                "productId" to productId,
                "qty" to itemQty,
                "shipping" to shipping
            )
        cartId.let {
            db.collection(firebaseController.docCart).document(it)
                .update("productlist", FieldValue.arrayUnion(data))
                .addOnSuccessListener {
                    Log.d("TAG", "Success update DataCart: ")

                }
                .addOnFailureListener{
                    Log.d("TAG", "update DataCart error: ")
                }
        }
    }
    fun checkCardId(id: String){
        val doc = db.collection(firebaseController.docCart).document(id)
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null){
                    cartId = FirebaseController.Userdata.uid.toString()
                }
            }
        }
    }

}