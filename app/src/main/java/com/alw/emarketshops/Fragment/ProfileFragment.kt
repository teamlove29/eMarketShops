package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.Activity.LoginActivity
import com.alw.emarketshops.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
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
            FirebaseController().updateUserData(user.displayName.toString(),user.uid)
           if (user.photoUrl !== null){
            Picasso.get().load(user.photoUrl)
//                .resize(100,100)
                .into(imageViewUser)

           }

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