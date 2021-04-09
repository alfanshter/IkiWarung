package com.alfanshter.iki_warung.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseInisalisasi {
    companion object{
        lateinit var auth : FirebaseAuth
        lateinit var firestore : FirebaseFirestore
        var UserId : String? = null

        fun Auth(){
            auth = FirebaseAuth.getInstance()
            UserId = auth.currentUser!!.uid
        }

        fun Firestore(){
            firestore = FirebaseFirestore.getInstance()
        }


    }

}