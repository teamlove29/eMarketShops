package com.alw.emarketshops.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alw.emarketshops.LoginActivity
import com.alw.emarketshops.R
import com.alw.emarketshops.RegistrtionActivity
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
        btnRegis.setOnClickListener(){
            val intent = Intent (getActivity(), LoginActivity::class.java)
            startActivity(intent)

        }

    }
}