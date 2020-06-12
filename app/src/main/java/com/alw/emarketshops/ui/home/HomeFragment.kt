package com.alw.emarketshops.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alw.emarketshops.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){

        /// Test CardView
        val  arrayList = ArrayList<ModelItemCard>()
        arrayList.add(ModelItemCard(R.drawable.p1,"หอมหัวใหญ่","12.00","50"))
        arrayList.add(ModelItemCard(R.drawable.p2,"ข้าวโพด","19.00","18"))
        arrayList.add(ModelItemCard(R.drawable.p3,"มะเขือเทศ","45.00","20"))
        arrayList.add(ModelItemCard(R.drawable.p5,"กะหล่ำปลี","16.00","44"))
        arrayList.add(ModelItemCard(R.drawable.p6,"กล้วยหอม","39.00","198"))
        arrayList.add(ModelItemCard(R.drawable.p7,"แอปเปิ้ลแดง","70.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p8,"กระเจี๊ยบเขียว","29.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p9,"แครอท","9.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p10,"สตรอเบอร์รี่","59.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p11,"แตงโม","49.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p12,"เลม่อน","39.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p12,"เลม่อน","39.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p12,"เลม่อน","39.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p12,"เลม่อน","39.00","180"))
        arrayList.add(ModelItemCard(R.drawable.p12,"เลม่อน","39.00","180"))


        val adapterItemCard = AdapterItemCard(arrayList,this)
        itemRecycler.layoutManager  = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        itemRecycler.adapter = adapterItemCard
        ////


    }


}