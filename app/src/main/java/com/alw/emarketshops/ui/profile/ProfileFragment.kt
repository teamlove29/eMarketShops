package com.alw.emarketshops.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.LoginActivity
import com.alw.emarketshops.MainActivity
import com.alw.emarketshops.R
import com.alw.emarketshops.ui.home.HomeFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        val user = FirebaseAuth.getInstance().currentUser

        if(user != null){
            textCurrenyUserName.text = user.displayName

        }else{
            textCurrenyUserName.text ="User : --"
        }

        btnRegisOrLogin.setOnClickListener{
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)

        }
        val context = this.context
        textCurrenyUserName.setOnClickListener {

            if (context != null) {
                AuthUI.getInstance()
                    .signOut(context)
                    .addOnCompleteListener {
                        Toast.makeText(context, "Sign Out successfully", Toast.LENGTH_SHORT).show()
                        textCurrenyUserName.text ="--"
                        FirebaseController.Userdata.uid = ""
                    }
            }
        }

    }
}