package com.alw.emarketshops.Model

import com.google.firebase.auth.FirebaseAuth

class ModelUser(){
    val user = FirebaseAuth.getInstance().currentUser
}