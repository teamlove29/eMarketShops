package com.alw.emarketshops.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.activity_suggestion.*

class ActivitySuggestion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)

        toolbarSugges.setOnClickListener {
            finish()
        }
    }
}