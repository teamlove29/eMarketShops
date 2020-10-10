package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_my_order.*

class ActivityMyOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        toolbarMyOrder.setOnClickListener {
            finish()
        }
    }
}