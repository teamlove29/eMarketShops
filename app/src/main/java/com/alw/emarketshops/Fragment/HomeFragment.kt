package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alw.emarketshops.*
import com.alw.emarketshops.Activity.ActivityCategory
import com.alw.emarketshops.Activity.ActivityChat
import com.alw.emarketshops.Activity.ActivityReQuotation
import com.alw.emarketshops.Adapter.AdapterItemCard
import com.alw.emarketshops.Adapter.ViewPagerAdapter
import com.alw.emarketshops.Model.ModelItemCard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*


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
            val url = "http://www.worldglovesthai.com/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
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
            "https://www.emarketshops.com/app-assets/img/slide/slide1.jpg",
            "https://www.emarketshops.com/app-assets/img/slide/slide2.jpg",
            "https://www.emarketshops.com/app-assets/img/slide/slide3.jpg"
        )
//        for (i in 0 until imageUrls.size)
//        {
//            Log.d("NUMBER", imageUrls[i])
//        }
        val adapter = ViewPagerAdapter(this, imageUrls)
        view_pager.adapter = adapter

    }

}