package com.alw.emarketshops

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnRegis.setOnClickListener {
            val i = Intent(this, RegistrtionActivity::class.java)
            startActivity(i)
            finish()
        }

        btnEmailRegis.setOnClickListener {

        }

    }
}