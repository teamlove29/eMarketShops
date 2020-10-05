package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_conditions.*

class ActivityConditions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conditions)

        conditionWeb.loadUrl("file:///android_asset/condition_page.html")
    }
}