package com.alw.emarketshops.Shipping

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class InterShipping {
    private val client = OkHttpClient().newBuilder().build()
    private val mediaType = "application/json".toMediaTypeOrNull()
    private val KEY = "9b1b8d2bf2924784a7ecb22829c3d910"
    private val SECRET = "c81c7b440a124616a63014c02d977102"


    fun auth_token() {
        val client = OkHttpClient().newBuilder()
            .build()

        val request = Request.Builder()
            .url("https://www.cash-pos.com/seller/getShippingToken")
            .method("LINK", null)
            .build()
        client.newCall(request).execute().use { response ->

            val responseData = response.body!!.string()
            val topic = Gson().fromJson(
                responseData,
                AuthResponse::class.java
            )
//            println("accessToken = ${topic.accessToken}")
//            println("refreshToken = ${topic.refreshToken}")

            OperationArea(topic.accessToken)
            Service(topic.accessToken)
        }

    }
    fun refresh_token(refreshToken : String) {

        val body = RequestBody.create(mediaType,
            "{\r\n    \"refreshToken\": \"$refreshToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://www.cash-pos.com/seller/getShippingRefreshToken")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()
            val topic = Gson().fromJson(
                responseData,
                AuthResponse::class.java
            )
            println("refreshToken2 = ${topic.refreshToken}")
        }

    }
    fun OperationArea(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingArea")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Operation_area>::class.java
            )
            for (dt in  data){
                println(dt.districtDesc)
            }
        }
    }
    fun Service(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingService")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Service>::class.java
            )
            for (dt in  data){
                println(dt.serviceDesc)
            }
        }
    }
    fun SericeControl(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingServiceControl")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Service_control>::class.java
            )
            for (dt in  data){
                println(dt.serviceControlDesc)
            }
        }
    }
    fun TemperatureControl(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingTemperatureControl")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Temperature_control>::class.java
            )
            for (dt in  data){
                println(dt.temperatureControlDesc)
            }
        }
    }
    fun TemperatureType(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingTemperatureType")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Temperature_type>::class.java
            )
            for (dt in  data){
                println(dt.temperatureTypeDesc)
            }
        }
    }
    fun PackageSize(accessToken:String){
        val body = RequestBody.create(mediaType,
            "{\r\n    \"accessToken\": \"$accessToken\"\r\n}"
        )
        val request = Request.Builder()
            .url("https://cash-pos.com/seller/getShippingPackageSize")
            .addHeader("X-IEL-KEY", KEY)
            .addHeader("X-IEL-SECRET", SECRET)
            .addHeader("Content-Type", "application/json")
            .method("POST", body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseData = response.body!!.string()

            val data = Gson().fromJson(
                responseData,
                Array<Package_size>::class.java
            )
            for (dt in  data){
                println(dt.sizeDesc)
            }
        }
    }

    data class AuthResponse(
        val ielApiKey: String,
        val accessToken: String,
        val refreshToken: String,
        val expiresDate: String
    )
    //Operation_area

    data class Operation_area (

        val provinceId : String,
        val provinceDesc : ProvinceDesc,
        val districtId : String,
        val districtDesc : DistrictDesc,
        val subDistrictId : String,
        val subDistrictDesc : SubDistrictDesc,
        val postcode : String
    )
    data class SubDistrictDesc (

        val th : String,
        val en : String
    )
    data class ProvinceDesc (

        val th : String,
        val en : String
    )
    data class DistrictDesc (

        val th : String,
        val en : String
    )

    // Service
    data class Service (

        val serviceCode : String,
        val serviceDesc : ServiceDesc
    )
    data class ServiceDesc (

        val th : String,
        val en : String
    )

    //Service-control
    data class Service_control (

        val serviceControlCode : String,
        val serviceControlDesc : ServiceControlDesc
    )
    data class ServiceControlDesc (

        val th : String,
        val en : String
    )

    //Temperature-control
    data class Temperature_control (

        val temperatureControlCode : String,
        val temperatureControlDesc : TemperatureControlDesc
    )
    data class TemperatureControlDesc (

        val th : String,
        val en : String
    )

    //Temperature-type
    data class Temperature_type (

        val temperatureTypeCode : String,
        val temperatureTypeDesc : TemperatureTypeDesc
    )
    data class TemperatureTypeDesc (

        val th : String,
        val en : String
    )

    //Package-size
    data class Package_size (

        val serviceCode : String,
        val serviceDesc : ServiceDesc,
        val serviceControlCode : String,
        val serviceControlDesc : ServiceControlDesc,
        val temperatureControlCode : String,
        val temperatureControlDesc : TemperatureControlDesc,
        val temperatureTypeCode : String,
        val temperatureTypeDesc : TemperatureTypeDesc,
        val sizeName : String,
        val sizeDesc : SizeDesc
    )
    data class SizeDesc (

        val th : String,
        val en : String
    )

    //shipment
    data class Shipment (
        val shipmentNo : String
        )
    data class Shipment_request (

        val shipmentNo : String,
        val shipmentRefNo1 : String,
        val shipmentRefNo2 : String,
        val serviceCode : String,
        val serviceControlCode : String,
        val temperatureTypeCode : String,
        val recipientName : String,
        val recipientPhoneNo : String,
        val recipientMobileNo1 : String,
        val recipientMobileNo2 : String,
        val recipientEmail : String,
        val recipientAddress : String,
        val recipientSubDistrictId : String,
        val recipientSubDistrictDesc : String,
        val recipientDistrictId : String,
        val recipientDistrictDesc : String,
        val recipientProvinceId : String,
        val recipientProvinceDesc : String,
        val recipientPostcode : String,
        val declareValue : String,
        val codAmount : String,
        val totalPackage : String,
        val specialDeliveryRemark : String,
        val originZoneId : String,
        val originZoneCode : String,
        val destinationZoneId : String,
        val destinationZoneCode : String,
        val packages : List<Packages>
    )
    data class Packages (

        val packageNo : String,
        val packageSize : String,
        val packageWidth : String,
        val packageLength : String,
        val packageHeight : String,
        val packageWeight : String,
        val remark : String
    )

    //Status
    data class Status (

        val trackingDt : String,
        val statusCode : String,
        val statusDesc : String,
        val location : String,
        val recipient : String,
        val latitude : Double,
        val longitude : Double
    )
    data class Shipment_status (

        val shipmentNo : String,
        val status : List<Status>
    )
}