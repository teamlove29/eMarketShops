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
                    val uri:Uri = Uri.parse(document["coverImage"].toString())

                    val list = listOf(document["images"])
                    val item = listOf(arrayOf(list[0])[0])
                    Log.d(TAG, "list: " + item[0])

//                    val map = document.data ****
//                    for ((key, value) in map) {
//                        if (key == "images") {
//                            Log.d("TAG", value.toString())
//                        }
//                    }

                    var stock:String  = document["stock"].toString()
                    if (stock == ""){stock = "0"}
//                    Log.d(TAG, "detail: $name")
                    newArrayList.add(ModelItemCard(uri, name,price,stock))
                }

            }
            arrayList = newArrayList
            Log.d(TAG, "arrayList.count : " + arrayList.count().toString())
            val adapterItemCard = AdapterItemCard(arrayList,this)
            itemRecycler.layoutManager  = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            itemRecycler.adapter = adapterItemCard
        }



}

}
