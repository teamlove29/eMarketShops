package com.alw.emarketshops.ui

import com.google.firebase.auth.FirebaseAuth

class ModelUser(){
    val user = FirebaseAuth.getInstance().currentUser
}