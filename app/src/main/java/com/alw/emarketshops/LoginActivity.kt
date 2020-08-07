package com.alw.emarketshops

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    private var RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        val googleUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (mAuth!!.currentUser !=null){
            Log.d("mAuth currentUser","User: " + mAuth!!.currentUser!!.email)
        }
        if(googleUser != null){
            Log.d("mAuth currentUser","googleUser : " + googleUser.displayName)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        btnRegis.setOnClickListener {
            val i = Intent(this, RegistrtionActivity::class.java)
            startActivity(i)
            finish()
        }

        btnEmailLogin.setOnClickListener {

        }

        btnGoogleLogin.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



    }
    override fun onStart() {
        super.onStart()
        val currentUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this) //mAuth!!.currentUser
        if (currentUser != null) {
//            txtDisplayName.text = currentUser.displayName
            Log.d("mAuth currentUser","googleUser : " + currentUser.displayName)
        }
//        else{txtDisplayName.text ="...."}
        //updateUI(currentUser)
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Google sign in successfully", Toast.LENGTH_SHORT).show()
            //updateUI(account)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: ApiException) {
            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "signInResult:failed code=" + e.statusCode, Toast.LENGTH_SHORT).show()
            updateUI(null)
        }
    }
    private  fun updateUI(user: GoogleSignInAccount?){
        if (user != null) {
            // Name, email address, and profile photo Url
            val name: String? = user.displayName
            val email: String?  = user.email
            val photoUrl: Uri?  = user.photoUrl
            val uid: String? = user.id
            val str: String  = "name: " + name +
                    "\nemail: " + email +
                    "\nuid: " + uid +
                    "\nphotoUrl: " + photoUrl
            Toast.makeText(this, str,
                Toast.LENGTH_SHORT).show()
        }
    }
}