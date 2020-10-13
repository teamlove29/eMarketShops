package com.alw.emarketshops.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.FirebaseController.Userdata.uid
import com.alw.emarketshops.Model.ModelItemCartList
import com.alw.emarketshops.OrderAPI
import com.alw.emarketshops.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_re_quotation.*
import kotlinx.android.synthetic.main.activity_select_payment.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ActivitySelectPayment : AppCompatActivity() {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val mediaType = MediaType.parse("application/json")
    private var order_id:String = ""
    val currentDate  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    val reference_order = getRandomString(50) //"ALW$currentDate"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)

        getShipping()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        textTotalpay.text = intent.getStringExtra("total")
        btnConfirm_pay.setOnClickListener {
            if (radioBtnQr.isChecked) {
                okHTTP(intent.getStringExtra("total"))
            }
            if (radioBtnCreditcard.isChecked){
                println("radioBtnCreditcard.isChecked")
                val inten = Intent(this, ActivityQrWeb::class.java)
                inten.putExtra("reference_order",reference_order)
                inten.putExtra("amount",textTotalpay.text.toString())
                inten.putExtra("shipping",spinnerShipping.selectedItem.toString())
                startActivity(inten)

            }
        }
        radioBtnQr.setOnClickListener {
            radioBtnBankpay.isChecked = !radioBtnQr.isChecked
            radioBtnCreditcard.isChecked = !radioBtnQr.isChecked

        }
        radioBtnBankpay.setOnClickListener {
            radioBtnQr.isChecked = !radioBtnBankpay.isChecked
            radioBtnCreditcard.isChecked = !radioBtnBankpay.isChecked
        }
        radioBtnCreditcard.setOnClickListener {
            radioBtnQr.isChecked = !radioBtnCreditcard.isChecked
            radioBtnBankpay.isChecked = !radioBtnCreditcard.isChecked
        }

        spinnerShipping.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                db.collection("shipping")
                    .whereEqualTo("name",spinnerShipping.selectedItem.toString())
                    .get()
                    .addOnCompleteListener {
                        for (doc in it.result!!){
                            textBaseCoast.text = doc?.get("baseCost")?.toString()
                        }
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }



    fun postJson_qr(amount: String):String {
        return ("{amount:$amount,"
                + "currency:THB,"
                + "description:TESTPRODUCT,"
                + "source_type:qr,"
                + "reference_order:ALW1234567,"
                + "metadata:[]"
                + "}")
    }

    fun okHTTP(amount: String){
        val body = RequestBody.create(
            mediaType,
            "{\r\n \"amount\": $amount," +
                    "\r\n \"currency\": \"THB\"," +
                    "\r\n \"description\": \"TESTPRODUCT\"," +
                    "\r\n \"source_type\": \"qr\"," +
                    "\r\n \"reference_order\": \"$reference_order\"," +
                    "\r\n \"metadata\":[]\r\n}"
        )
        val request = Request.Builder()
            .url(OrderAPI().url+"/qr/v2/order")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body()!!.string()
            val topic = Gson().fromJson(responseData,
                OrderAPI.Json4Kotlin_Base::class.java)
            order_id=topic.id
            qrCheckout(topic.id, amount)
        }

    }
    fun qrCheckout(id: String, amount: String){
        val body = RequestBody.create(
            mediaType,
            "{\r\n \"order_id\": \"$id\"," +
                    "\r\n \"amount\": \"$amount\"," +
                    "\r\n \"currency\": \"THB\"," +
                    "\r\n \"description\": \"TESTPRODUCT\"," +
                    "\r\n \"sof\": \"ThaiQR\"," +
                    "\r\n \"reference_order\": \"$reference_order\"," +
                    "\r\n \"metadata\":[]\r\n}"
        )
        val request = Request.Builder()
            .url(OrderAPI().url+"/qr/v2/qr")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body()!!.string()
            println(responseData)
            val topic = Gson().fromJson(responseData, OrderAPI.qrResponse::class.java)
            println(topic.id)

        val inten = Intent(this, ActivityQrthai::class.java)
            inten.putExtra("response", responseData)
            inten.putExtra("amount", amount)
            inten.putExtra("order_id", id)
            inten.putExtra("reference_order",reference_order)
            inten.putExtra("shipping",spinnerShipping.selectedItem.toString())
            startActivity(inten)
            finish()
        }
    }

    fun createCharge(amount:String,token:String){
        val body = RequestBody.create(
            mediaType,
            "{\r\n \"amount\": $amount," +
                    "\r\n \"currency\": \"THB\"," +
                    "\r\n \"description\": \"TESTPRODUCT\"," +
                    "\r\n \"source_type\": \"card\"," +
                    "\r\n \"mode\": \"token\"," +
                    "\r\n \"token\": \"$token\"," +
                    "\r\n \"reference_order\": \"$reference_order\"," +
                    "\r\n \"ref_1\": \"ref1\"," +
                    "\r\n \"ref_2\":\"123456\r\n}"
        )
        val request = Request.Builder()
            .url(OrderAPI().url+"/card/v2/charge")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", OrderAPI().skey)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body()!!.string()
            println(responseData)
        }

    }

    fun creatOrderData(paymentTypeid:Int,reference_order:String,order_id:String,shipping:String){
        println(reference_order)
        var paymentType = ""
        when (paymentTypeid) {
            1 -> {
                paymentType = "qr"
            }
            2 -> {
                paymentType ="banktransfer"
            }
            3 -> {
                paymentType ="creditcard"
            }
        }
//       var dataAddress: HashMap<String,Any>? = null
        val dbAddress = db.collection("userProfile").document(uid!!)
        dbAddress.get().addOnSuccessListener{
           val dataAddress = hashMapOf(
                "address" to "${it["address"].toString()} ${it["subdistrict"].toString()} " +
                        "${it["district"].toString()} ${it["province"].toString()}",
                "name" to it["receivename"].toString(),
                "phone" to it["phone"].toString(),
                "postcode" to it["zipcode"].toString()
            )


        val doc = db.collection("cart")
            .document(uid.toString())
        doc.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                if (documentSnapshot.data !== null) {
                    val arrayList = ArrayList<Any>()
                    var itemdata: MutableMap<*, *>? = null
                    var totalCart: Long = 0
                    val map: MutableMap<*, *>? = documentSnapshot.data
                    for (entry in map!!.entries) {
                        val list = entry.value as ArrayList<*>
                        for (each in list) {
                             itemdata = each as MutableMap<*, *>?
                            if (itemdata != null) {
                                println(itemdata["brandId"].toString() + itemdata["name"].toString())
                                val price: String = itemdata["price"].toString()
                                val qty: String = itemdata["qty"].toString()
                                val itemOrder = hashMapOf(
                                    "qty" to qty,
                                    "total" to (price.toLong() * qty.toLong()).toString(),
                                    "subTotal" to (price.toLong() * qty.toLong()).toString(),
                                    "pricePerUnit" to price,
                                    "productName" to itemdata["name"].toString(),
                                    "productId" to itemdata["productId"].toString()
                                )
                                totalCart += (price.toLong() * qty.toLong())
                                arrayList.add(itemOrder)
                            }

                        }
                            val paymentDetail = hashMapOf(
                                "total" to totalCart,
                                "shippingCost" to "0",
                                "paymentType" to paymentType
                            )
                            val data = hashMapOf(
                                "cancelDate" to "",
                                "customerId" to uid.toString(),
                                "insertDate" to Timestamp.now(),
                                "isActive" to true,
                                "orderItem" to arrayList,
                                "orderNo" to order_id,
                                "paymentDetail" to paymentDetail,
                                "processing" to "not processed",
                                "reference_order" to reference_order,
                                "sellerId" to itemdata?.get("brandId").toString(),
                                "serviceCost" to "0",
                                "shipping" to shipping,
                                "shippingAddress" to dataAddress,
                                "status" to "Processing",
                                "total" to totalCart,
                                "tracking" to "${getRandomString(3)}${currentDate}TH"
                            )
                        println(data)
                            val orderList = hashMapOf(
                                "orderList" to listOf(data)
                            )

                        db.collection("orders").document(itemdata?.get("brandId").toString()+"_test")
                            .get().addOnSuccessListener{
                                if (it.data !== null) {
                                    db.collection("orders")
                                        .document(itemdata?.get("brandId").toString() + "_test")
                                        .update("orderList", FieldValue.arrayUnion(data))
                                        .addOnSuccessListener {
                                            Log.d("TAG", "Success insert DataOrder: ")
                                        }
                                }else{
                                    db.collection("orders")
                                        .document(itemdata?.get("brandId").toString() + "_test")
                                        .set(orderList as Map<*, *>)
                                        .addOnSuccessListener {
                                            Log.d("TAG", "Success insert DataOrder: ")
                                        }
                                }
                            }
                        }



                } else {
                    Toast.makeText(this, "no cart data", Toast.LENGTH_SHORT).show()
                }
            }

            }
        }

    }

    private fun getShipping(){
        var spp_adapter: ArrayAdapter<String>?
        db.collection("shipping")
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<String>()
                if (task.isSuccessful){
                    for (doc in task.result!!){

                        Log.d("task.result", task.result.toString())
                        val name : String = doc["name"].toString()

                        newArrayList.add(name)
                    }
                }
                spp_adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    newArrayList
                )
                spp_adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerShipping!!.adapter = spp_adapter

            }
    }

    companion object {
        private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
    }

    private fun getRandomString(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }
}


