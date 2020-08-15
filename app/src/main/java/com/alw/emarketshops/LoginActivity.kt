package com.alw.emarketshops

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)


        btnRegis.setOnClickListener {
            val i = Intent(this, RegistrtionActivity::class.java)
            startActivity(i)
            finish()
        }

        btnEmailLogin.setOnClickListener {

        }

        btnGoogleLogin.setOnClickListener {
//            startActivityForResult(
//                AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setAvailableProviders(providers)
//                    .build(),
//                RC_SIGN_IN)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Log.d("TAG", "firebaseAuth:" +  user.uid)
                    Toast.makeText(this, "Google sign in successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else {
                Log.w("TAG", "Google sign in failed")
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
//    override fun onStart() {
//        super.onStart()
//        val currentUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this) //mAuth!!.currentUser
//        if (currentUser != null) {
////            txtDisplayName.text = currentUser.displayName
//            Log.d("mAuth currentUser","googleUser : " + currentUser.displayName)
//        }
////        else{txtDisplayName.text ="...."}
//        //updateUI(currentUser)
//    }
//    @Override
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//        try {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val account = task.getResult(ApiException::class.java)!!
//            Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
////            handleSignInResult(task)
//            firebaseAuthWithGoogle(R.string.default_web_client_id.toString())
//        }catch (e: Exception){
//            Log.w("TAG", "Google sign in failed", e)
//        }
//
//
//        }
//    }
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//
//            // Signed in successfully, show authenticated UI.
//            Toast.makeText(this, "Google sign in successfully", Toast.LENGTH_SHORT).show()
//            //updateUI(account)
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        } catch (e: ApiException) {
//            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
//            Toast.makeText(this, "signInResult:failed code=" + e.statusCode, Toast.LENGTH_SHORT).show()
//            updateUI(null)
//        }
//    }
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        mAuth?.signInWithCredential(credential)
//            ?.addOnCompleteListener {
//
//                if (it.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success" + (it.result?.user?.uid))
//                    val user = mAuth?.currentUser
//    //                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("TAG", "signInWithCredential:failure", it.exception)
//
//                    updateUI(null)
//                }
//
//                // ...
//            }
//    }
//    private  fun updateUI(user: GoogleSignInAccount?){
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            val name: String? = user.displayName
//            val email: String?  = user.email
//            val photoUrl: Uri?  = user.photoUrl
//            val uid: String? = user.id
//            val str: String  = "name: " + name +
//                    "\nemail: " + email +
//                    "\nuid: " + uid +
//                    "\nphotoUrl: " + photoUrl
//            Toast.makeText(this, str,
//                Toast.LENGTH_SHORT).show()
//        }
//    }
}