package com.alw.emarketshops

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.ui.ModelUser
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
    lateinit var brandId :String
    lateinit var categoryCode:String
    lateinit var categoryMainCode:String
    lateinit var categorySubCode:String
    lateinit var productId:String
    lateinit var shopName:String

    val modelUser = ModelUser()
    var cartId: String? = null
    var itemQty:String = "1"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        checkCardId(modelUser.user?.uid.toString())

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
                categoryCode = taskproduct.result?.get("categoryCode").toString()
                categoryMainCode = taskproduct.result?.get("categoryMainCode").toString()
                categorySubCode = taskproduct.result?.get("categorySubCode").toString()
                productId = id

                db.collection("shops").document(taskproduct.result?.get("userId").toString())
                    .get().addOnCompleteListener { taskShop  ->
                        Log.d("shopName",taskShop.result?.get("shopName").toString())
                        shopName = taskShop.result?.get("shopName").toString()
                    }
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
                "shipping" to ""
            )
            val productList = hashMapOf(
                "productlist" to listOf(itemdata)
            )

            db.collection("cart_android_test").document(modelUser.user?.uid.toString())
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
    fun cartdataPut(cartId:String){


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
                "shipping" to ""
            )
        cartId.let {
            db.collection("cart_android_test").document(it)
                .update("productlist" , FieldValue.arrayUnion(data))
                .addOnSuccessListener {
                    Log.d("TAG", "Success update DataCart: ")

                }
                .addOnFailureListener{
                    Log.d("TAG", "update DataCart error: ")
                }
        }
    }
    fun checkCardId(id:String){
        val doc = db.collection("cart_android_test").document(id)
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null){
                    cartId = modelUser.user?.uid
                }
            }
        }
    }

}