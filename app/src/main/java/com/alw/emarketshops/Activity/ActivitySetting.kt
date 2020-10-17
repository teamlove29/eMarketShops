package com.alw.emarketshops.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.R
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_setting.*

class ActivitySetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnLogout.setOnClickListener {

                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Sign Out successfully", Toast.LENGTH_SHORT).show()
                        FirebaseController.Userdata.uid = ""
                    }
        }
        toolbarSetting.setOnClickListener {
            this.finish()
        }

        btnCondition.setOnClickListener {
            val intent = Intent (this, ActivityConditions::class.java)
            startActivity(intent)
        }
        btnCommu.setOnClickListener {
            val intent = Intent (this, ActivitySuggestion::class.java)
            startActivity(intent)
        }
    }
}