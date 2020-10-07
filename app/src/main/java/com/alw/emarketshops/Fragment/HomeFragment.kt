package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.*
import com.alw.emarketshops.Activity.*
import com.alw.emarketshops.Adapter.AdapterItemCard
import com.alw.emarketshops.Adapter.AdapterItemList
import com.alw.emarketshops.Adapter.ViewPagerAdapter
import com.alw.emarketshops.FirebaseController.Firebase.db
import com.alw.emarketshops.Model.ChatMessage
import com.alw.emarketshops.Model.ModelItemCard
import com.alw.emarketshops.Model.ModelItemCartList
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DecimalFormat


class HomeFragment : Fragment() {

//    private val db = FirebaseFirestore.getInstance()
    private var arrayList = ArrayList<ModelItemCard>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        insertSlideImg()
        getList()


        btnCategory.setOnClickListener {
            val inten =Intent(activity, ActivityCategory::class.java)
            startActivity(inten)
        }

        btnQuotation.setOnClickListener {
            val inten =Intent(activity, ActivityReQuotation::class.java)
            startActivity(inten)
        }
        btnWeb.setOnClickListener {
            val inten =Intent(activity, ActivityWorldInc::class.java)
            startActivity(inten)
        }

        btnProductActive.setOnClickListener {
//            val i = Intent(activity, ActivityProducts::class.java)
//            startActivity(i)
        }
    }

private fun getList() {

    db.collection("product")
        .whereEqualTo("isActive", true).whereEqualTo("isReady", true)
        .get()
        .addOnCompleteListener { task  ->
            val newArrayList = ArrayList<ModelItemCard>()
            if (task.isSuccessful){
                for (document in task.result!!){

                    val name : String = document["name"].toString()
                    val price :String = document["price"].toString()

                    var uri = Uri.parse("")
                    val dt:Any? = document["images"]
                    val ls = dt as ArrayList<Any>
                    for ((index, each) in ls.withIndex()){
                        val imgdata: MutableMap<*, *>? = each as MutableMap<*, *>?
                        if (imgdata !== null && index == 0){
                            uri = Uri.parse(imgdata.get("imgUrl").toString())
                        }
                    }
                    val id:String = document.id
                    val detail:String = document["detail"].toString()
                    val brand:String = document["brand"].toString()
                    var stock:String  = document["stock"].toString()
                    if (stock == ""){stock = "0"}
                    newArrayList.add(ModelItemCard(id, uri, name, price, stock, detail, brand))
                }

            }
            arrayList = newArrayList
            val context: Context? = this.context
            val adapterItemCard = context?.let { AdapterItemCard(arrayList, it) }
            itemRecycler.layoutManager  = GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            itemRecycler.adapter = adapterItemCard
        }



}
   fun rebuildUrl(list: List<Any>, index: Int):Uri{
        val nList = list[index].toString()
            .replace("[", "")
            .replace("]", "")
        val strs = nList.split(",").toTypedArray()
        return Uri.parse(strs[index].substringAfter("=").substringBefore("}"))
   }

    fun insertSlideImg(){
//        Picasso.get().load("https://www.emarketshops.com/app-assets/img/slide/slide2.jpg")
//            .resize(600,440).into(imageTopAD)

        val imageUrls = arrayOf(
            "https://www.emarketshops.com/app-assets/img/slide/slide1.jpg"//,
//            "https://www.emarketshops.com/app-assets/img/slide/slide2.jpg",
//            "https://www.emarketshops.com/app-assets/img/slide/slide3.jpg"
        )
//        for (i in 0 until imageUrls.size)
//        {
//            Log.d("NUMBER", imageUrls[i])
//        }
        val adapter = ViewPagerAdapter(this, imageUrls)
        view_pager.adapter = adapter

    }
    fun getChilEvenMessage(){
        FirebaseDatabase.getInstance()
            .getReference("messages").child(FirebaseController.Userdata.uid.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

//                    Log.d("snapshot.key",snapshot.child(snapshot.key.toString()).toString())
//                        notifi( snapshot.child("message").value.toString(),
//                                snapshot.child("uid").value.toString(),
//                                snapshot.child("userchat_name").value.toString()
//                            )
//

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("homefrag >>", "onDataChange")

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun notifi(message :String,uid:String,sendername:String){
        val bitmap = BitmapFactory.decodeResource(
            this.resources,
            R.drawable.emarket_logo)
        val context: Context? = this.context
        val NOTIFICATION_ID = 234
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            val CHANNEL_ID = "eMarketShops"
            val name = "eMarketShops"
            val Description = "eMarketShops Chat"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }


        val builder = context?.let {
            NotificationCompat.Builder(it, "my_channel_01")
                .setSmallIcon(R.drawable.outline_announcement_black_18dp)
                .setContentTitle(sendername)
                .setLargeIcon(bitmap)
                .setContentText(message)
        }
        val resultIntent = Intent(context, ActivityChat::class.java)
        resultIntent.putExtra("brand", sendername)
        resultIntent.putExtra("brandId", uid)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(ActivityChat::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(resultPendingIntent)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }



}
