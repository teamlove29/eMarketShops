package com.alw.emarketshops

class OrderAPI {

    val url: String = "https://dev-kpaymentgateway-services.kasikornbank.com"
    val skey: String = "skey_test_20650YXYgziqkAKF23bFV8xwqmBarX3xjUL5W"
    val pkey: String = "pkey_test_20650YFBiHqxuHpyGfCrkoQpL6yfag8RkJak5"

    data class qrOrderResponse(

        val id: String,
        val obj: String,
        val created: String,
        val livemode: Boolean,
        val amount: String,
        val currency: String,
        val customer: String,
        val description: String,
        val metadata: List<String>,
        val status: String,
        val reference_order: String,
        val source_type: String,
        val additional_data: Additional_data,
        val failure_code: String,
        val failure_message: String,
        val expire_time_seconds: Int
    )
    data class Additional_data(

        val term: String,
        val mid: String,
        val tid: String,
        val smartpay_id: String,
        val campaign_id: String
    )
    data class qrResponse(

        val id : String,
        val obj : String,
        val account_name : String,
        val order_id : String,
        val paint_text : String,
        val image_with_base64 : String,
        val sof_support : List<Sof_support>,
        val status : String,
        val created : String,
        val livemode : Boolean,
        val failure_code: String,
        val failure_message: String,
        val expire_time_seconds: Int

    )

    data class Sof_support (

        val code : String,
        val desc : String,
        val icon_url : String
    )
    data class inquiryOrder (

        val id : String,
        val obj : String,
        val amount : String,
        val currency : String,
        val reference_order : String,
        val source_type : String,
        val customer : Customer,
        val additional_data : Additional_data,
        val description : String,
        val metadata : List<Metadata>,
        val status : String,
        val created : Int,
        val livemode : Boolean,
        val failure_code : String,
        val failure_message : String,
        val expire_time_seconds : Int
    )
    data class Customer (

        val customer_id : String
    )
    data class Qr_inquiry (

        val id : String,
        val order_id : String,
        val obj : String,
        val amount : String,
        val currency : String,
        val transaction_state : String,
        val source : Source,
        val created : String,
        val status : String,
        val reference_order : String,
        val description : String,
        val livemode : Boolean,
        val metadata : String,
        val failure_code : String,
        val failure_message : String
    )
    data class Source (

        val id : String,
        val obj : String,
        val brand : String
    )

    data class change (
//        val uid : String,
        val id : String,
        val obj : String,
        val amount : String,
        val currency : String,
        val transaction_state : String,
        val source : ChangeSource,
        val created : String,
        val status : String,
        val livemode : String,
//        val metadata : Metadata,
        val failure_code : String,
        val failure_message : String,
        val mpi : Mpi,
        val description : String,
        val redirect_url : String,
        val settlement_info : String,
        val refund_info : String,
        val approval_code : String,
        val multi_clearing : String,
        val ref_1 : String,
        val campaign_id : String,
        val ref_2 : String,
        val ref_3 : String,
        val dcc_data : String,
        val reference_order : String
    )
    data class Mpi (

        val eci : String,
        val xid : String,
        val cavv : String,
        val kbank_mpi : String
    )
    data class ChangeSource (

        val id : String,
        val obj : String,
        val brand : String,
        val card_masking : String,
        val issuer_bank : String
    )
}