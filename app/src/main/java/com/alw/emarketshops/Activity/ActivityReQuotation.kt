package com.alw.emarketshops.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.android.synthetic.main.activity_re_quotation.*
import java.util.concurrent.TimeUnit


class ActivityReQuotation : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var cate_adapter:ArrayAdapter<String>? = null
    private var catetype_adapter:ArrayAdapter<String>? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_quotation)

        getCategoryList()

        val i = intent
        if (i.getStringExtra("productName") !== null){

            val brand = " ${i.getStringExtra("brand")}"

            txtProductname.setText(i.getStringExtra("productName")+ brand)
            Log.d("categoryName",i.getStringExtra("categoryName"))
//            catetype_adapter?.getPosition(i.getStringExtra("categoryName"))?.let { spinnerCategorytype.setSelection(it) }
//            getcategoryTypelist(spinnerCategory.selectedItem.toString())
        }



        btnSendReQuo.setOnClickListener {

            if (checkBoxAllow.isChecked){
                sendQuotationRe()

            }else{
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("")
                dialogBuilder.setMessage("กรุณายินยอมข้อตกลง")
                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                }
                dialogBuilder.show()
            }


        }
        btnQuoList.setOnClickListener {

            val intent = Intent (this, ActivityMyQuotation::class.java)
            startActivity(intent)
        }

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getcategoryTypelist(spinnerCategory.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }
    private fun getCategoryList(){
        db.collection("category")
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<String>()
               if (task.isSuccessful){
                   for (doc in task.result!!){

                       Log.d("task.result", task.result.toString())
                       val code : String = doc["cateCode"].toString()
                       val name : String = doc["nameTH"].toString()

                       newArrayList.add(name)
                   }
               }
                cate_adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    newArrayList
                )
                cate_adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory!!.adapter = cate_adapter

            }
    }
    fun getcategoryTypelist(name: String){
        db.collection("category").whereEqualTo("nameTH", name)
            .get()
            .addOnCompleteListener { task  ->
                val newArrayList = ArrayList<String>()
                if (task.isSuccessful){
                    for (doc in task.result!!){
                        db.collection("category").document(doc["cateCode"].toString())
                            .get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot != null) {
                                    if (documentSnapshot.data !== null) {
                                        val list = documentSnapshot["subs"] as ArrayList<Any>
                                        for (each in list) {
                                            val catedata: MutableMap<*, *>? = each as MutableMap<*, *>?
                                            val name : String = catedata?.get("nameTH").toString()
                                            newArrayList.add(name)
                                        }
                                    }else{
                                        spinnerCategorytype.removeAllViews()
                                    }
                                }

                                catetype_adapter = ArrayAdapter(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    newArrayList
                                )
                                catetype_adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinnerCategorytype!!.adapter = catetype_adapter
                        }
                    }

                }

            }
    }

    @SuppressLint("SimpleDateFormat")
    val currentDate  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    val quoNo = FirebaseController.Userdata.uid.toString() + currentDate
    fun sendQuotationRe(){
        Log.d("quoNo", quoNo)
        val itemdata = hashMapOf(
            "amountDC" to "0",
            "priceUnit" to txtPriceunit.text.toString(),
            "productCode" to "",
            "productId" to "",
            "productName" to txtProductname.text.toString(),
            "productQty" to txtQty.text.toString(),
            "subtotal" to "",
            "tax" to "",
            "total" to ""
        )

        val data_quo = hashMapOf(
            "QuotationDate" to Timestamp.now(),
            "QuotationNo" to quoNo,
            "amountDC" to "0",
            "customerAddress" to txtAddress.text.toString(),
            "customerName" to FirebaseController.Userdata.name.toString(),
            "description" to txtdescription.text.toString(),
            "insertDate" to Timestamp.now(),
            "isActive" to true,
            "quotationItem" to listOf(itemdata),
            "sellerId" to "",
            "subject" to "",
            "subtotal" to "",
            "tax" to "",
            "total" to "",
            "qty" to txtQty.text.toString(),
            "shipping" to txtShipping.text.toString(),
            "shippingTime" to txtShippingTime.text.toString(),
            "category" to spinnerCategory.selectedItem.toString(),
            "subCategory" to spinnerCategorytype.selectedItem.toString()
        )
        val quotation = hashMapOf(
            "quotation" to listOf(data_quo)
        )
        db.collection("quotation").document(FirebaseController.Userdata.uid.toString())
            .get().addOnSuccessListener {
                if (it !== null) {
                    if (it.data !== null) {

                        creatPDF()

                        Log.d("", "update")
                        db.collection("quotation")
                            .document(FirebaseController.Userdata.uid.toString())
                            .update("quotation", FieldValue.arrayUnion(data_quo))
                            .addOnSuccessListener {
                                val dialogBuilder = AlertDialog.Builder(this)
                                dialogBuilder.setTitle("")
                                dialogBuilder.setMessage("ส่งคำขอเสนอราคาเรียบร้อย")
                                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                                    finish()
                                }
                                dialogBuilder.show()
                            }
                    } else {
                        Log.d("", "set")
                        db.collection("quotation")
                            .document(FirebaseController.Userdata.uid.toString())
                            .set(quotation as Map<String, Any>)
                            .addOnSuccessListener {
                                val dialogBuilder = AlertDialog.Builder(this)
                                dialogBuilder.setTitle("")
                                dialogBuilder.setMessage("ส่งคำขอเสนอราเรียบร้อย")
                                dialogBuilder.setPositiveButton("ตกลง") { _, _ ->
                                    finish()
                                    dialogBuilder.show()
                                }
                            }
                    }
                }
            }
    }
    fun creatPDF(){
        val content = "${txtdescription.text}/n" +
                "Product name :${txtProductname.text}/n"+
                "qty: ${txtQty.text}/n"+
                "Price : ${txtPriceunit.text}/n"+
                "Quotation No. ${quoNo}"

        CreatePdf(this)
            .setPdfName(txtProductname.text.toString() + currentDate)
            .openPrintDialog(false)
            .setContentBaseUrl(null)
            .setPageSize(PrintAttributes.MediaSize.ISO_A4)
            .setContent(content)
            .setFilePath(Environment.getExternalStorageDirectory().absolutePath + "/Documents")
            .setCallbackListener(object : CreatePdf.PdfCallbackListener {
                override fun onFailure(errorMsg: String) {
                    Log.d("CreatePdf errorMsg", errorMsg)
                }

                override fun onSuccess(filePath: String) {
                    Log.d("CreatePdf filePath", filePath)
                }
            })
            .create()
    }

}