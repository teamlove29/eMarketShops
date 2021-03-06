package com.alw.emarketshops.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.alw.emarketshops.Activity.*
import com.alw.emarketshops.FirebaseController
import com.alw.emarketshops.R
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
        println(user?.email)
        if(user != null){
            btnRegisOrLogin.isVisible = false
            textCurrenyUserName.text = "${user.email} \n ${user.displayName!!}"
//            if (user.displayName == ""){
//                textCurrenyUserName.text = user.email
//            }else{
//                textCurrenyUserName.text = user.displayName
//
//            }

            FirebaseController().updateUserData(user.displayName.toString(), user.uid)
           if (user.photoUrl !== null){
            Picasso.get().load(user.photoUrl)
//                .resize(100,100)
                .into(imageViewUser)

           }else{
               imageViewUser.setImageResource(R.drawable.baseline_account_circle_black_24dp)
           }

        }else{
            textCurrenyUserName.text ="User : --"
        }

        btnRegisOrLogin.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }

        btnSetting.setOnClickListener {
            val intent = Intent(activity, ActivitySetting::class.java)
            startActivity(intent)
        }

        btnAddress.setOnClickListener {
            val intent = Intent(activity, ActivityAddress::class.java)
            startActivity(intent)
        }
        btnViewQualist.setOnClickListener {
            val intent = Intent(activity, ActivityMyQuotation::class.java)
            startActivity(intent)
        }
        btnInformation.setOnClickListener {
            val intent = Intent(activity, ActivityInformation::class.java)
            startActivity(intent)
        }
        btnMyOrders.setOnClickListener {
            val intent = Intent(activity, ActivityMyOrder::class.java)
            startActivity(intent)
        }
        btnMycart.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_profile_to_navigation_cart)
        }

    }


}