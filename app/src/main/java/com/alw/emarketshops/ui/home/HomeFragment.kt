package com.alw.emarketshops.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.AdapterItemCard
import com.alw.emarketshops.ModelItemCard
import com.alw.emarketshops.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_card.view.*

class HomeFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
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
        Picasso.get().load("https://www.emarketshops.com/app-assets/img/slide/slide2.jpg").resize(600,600).into(imageTopAD)
        getList()
    }

private fun getList() {

    db.collection("product")
        .whereEqualTo("isActive", true).whereEqualTo("isReady",true)
        .get()
        .addOnCompleteListener { task  ->
            val newArrayList = ArrayList<ModelItemCard>()
            if (task.isSuccessful){
                for (document in task.result!!){

                    val name : String = document["name"].toString()
                    val price :String = document["price"].toString()
                    val list = listOf(document["images"])

                    Log.d(TAG, "list removePrefix : " + list[0].toString().replace("[","").replace("]",""))
                    val uri = Uri.parse(rebuildUrl(list as List<Any>,0))

                    val detail: String = document["detail"].toString()
                    val brand: String = document["brand"].toString()
                    var stock:String  = document["stock"].toString()
                    if (stock == ""){stock = "0"}
                    newArrayList.add(ModelItemCard(uri, name,price,stock,detail,brand))
                }

            }
            arrayList = newArrayList
            Log.d(TAG, "arrayList.count : " + arrayList.count().toString())
            val adapterItemCard = AdapterItemCard(arrayList,this)
            itemRecycler.layoutManager  = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            itemRecycler.adapter = adapterItemCard
        }



}
   fun rebuildUrl(list:List<Any>,index:Int):String{
        val nList = list[index].toString().replace("[","").replace("]","")
        val strs = nList.split(",").toTypedArray()
        return strs[index].substringAfter("=").substringBefore("}")
   }

}
