package com.alw.emarketshops

import okhttp3.*
import java.io.IOException


class Post {
    val JSON = MediaType.parse("application/json; charset=utf-8")
    private var client = OkHttpClient()
    @Throws(IOException::class)
    internal fun post(url: String, json : HashMap<String, String>): Call {
        val builder = FormBody.Builder()
        val it = json.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }
        val body:RequestBody = builder.build() //RequestBody.create(JSON, json)

        val request = Request.Builder()
            .method("POST",body)
            .addHeader("Content-Type","application/json")
            .addHeader("x-api-key","skey_test_20650YXYgziqkAKF23bFV8xwqmBarX3xjUL5W")
            .url(url)
            .post(body)
            .build()

//        client.newCall(request)
//            .execute()
//            .use { response ->
//            return response.body()?.string() }
        val call = client.newCall(request)
        return call
    }
    internal fun postJson(amount: String):String {
        return ("{'amount':$amount,"
                + "'currency':'THB',"
                + "'description':'TESTPRODUCT',"
                + "'source_type':card,"
                + "'mode':'token',"
                + "'token':'tokn_prod_1fadeb16520513a076c2d97df3b0841f9',"
                + "'reference_order':'20180530175600',"
                + "'ref_1':'ref1',"
                + "'ref_2':'123456',"
                + "}")
    }
    internal fun postJson_qr(amount: String):String {
        return ("{amount:$amount,"
                + "currency:THB,"
                + "description:TESTPRODUCT,"
                + "source_type:qr,"
                + "reference_order:ALW1234567,"
                + "metadata:[]"
                + "}")
    }

    internal fun  token(){
//        val apiKey = "sk_test_BQokikJOvBiI2HlWgH4olfQ2"
//        val token: String = request.getParameter("token")
//        var client: Api.Client? = null
//
//        val ep: Endpoint = configService.getEndpoint()
//
//        client = Client(ep, apiKey)
//        val chargeModel: Builder =
//            Builder().amount(100).currency("THB").sof("card").description(description).card(token)
//        val charge: Charge = Charge.create(params)
    }
//    companion object {
//        val JSON = MediaType.parse("application/json; charset=utf-8")
//        @Throws(IOException::class)
//        @JvmStatic fun main(args:Array<String>) {
//            val post = Post()
//            val json = post.postJson("500.99")
//            val response = post.post("https://dev-kpaymentgateway-services.kasikornbank.com", json)
//            println(response)
//        }
//    }
}