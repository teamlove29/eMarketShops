package com.alw.emarketshops.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alw.emarketshops.LoginActivity
import com.alw.emarketshops.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        mAuth = FirebaseAuth.getInstance()
        val googleUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity)
        if (mAuth!!.currentUser !=null){
            Log.d("mAuth currentUser","User: " + mAuth!!.currentUser!!.email)
            textCurrenyUserName.text = mAuth!!.currentUser!!.email
        }
        if(googleUser != null){
            textCurrenyUserName.text ="User :" + googleUser.displayName
        }else{
            textCurrenyUserName.text ="User : --"
        }

        btnRegisOrLogin.setOnClickListener{
            val intent = Intent (activity, LoginActivity::class.java)
            startActivity(intent)

        }

    }
}