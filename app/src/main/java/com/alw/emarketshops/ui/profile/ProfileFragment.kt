package com.alw.emarketshops.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alw.emarketshops.LoginActivity
import com.alw.emarketshops.R
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        btnRegisOrLogin.setOnClickListener(){
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)

        }

    }
}